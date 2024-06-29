package systems.monomer.interpreter.controls;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.interpreter.values.InterpretBreaking;
import systems.monomer.interpreter.values.InterpretCollection;
import systems.monomer.interpreter.values.InterpretSequence;
import systems.monomer.syntaxtree.controls.ForNode;
import static systems.monomer.interpreter.controls.InterpretControls.InterpretControlResult;

import java.util.Iterator;

public class InterpretForNode extends ForNode implements InterpretControlNode {
    public InterpretControlResult interpretControl(boolean previousSuccess, boolean previousFailure, InterpretValue previousValue) {
        InterpretNode firstNode = getFirstInterpretNode();
        InterpretNode collection = firstNode.getSecondInterpretNode();

        InterpretResult collectionResult = collection.interpretValue();
        if(!collectionResult.isValue()) return new InterpretControlResult(false, collectionResult); //return statement

        InterpretValue maybeIterable = collectionResult.asValue();

        if(maybeIterable instanceof InterpretCollection iterable) {
            Iterator<? extends InterpretValue> iter = iterable.iterator();
            InterpretSequence ret = new InterpretSequence(iterable.getElementType());

            while(iter.hasNext()) {
                initVariables();
                InterpretValue val = iter.next();
                iteratorKey.setValue(val);


                //TODO set iterator variable within the Monomer loop
                InterpretResult result = getSecondInterpretNode().interpretValue();
                if(result.isValue())
                    ret.add(result.asValue());
                else {
                    InterpretControlResult controlResult = InterpretControls.resultOfBreak((InterpretBreaking) result, isThisExpression());
                    if (controlResult != null) return controlResult;
                }
            }
            return new InterpretControlResult(true, ret);
        } else {
            throw getFirst().syntaxError("For operator requires a collection for the control of repetitions");
        }
    }

    @Override
    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot assign to result of for loop");
    }
}
