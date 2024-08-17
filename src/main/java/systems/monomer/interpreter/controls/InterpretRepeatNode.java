package systems.monomer.interpreter.controls;

import systems.monomer.interpreter.InterpretLocality;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.values.InterpretInt;
import systems.monomer.interpreter.variables.InterpretVariable;
import systems.monomer.interpreter.values.InterpretBreaking;
import systems.monomer.interpreter.values.InterpretSequence;
import systems.monomer.syntaxtree.controls.RepeatNode;

import static systems.monomer.interpreter.controls.InterpretControls.InterpretControlResult;

public class InterpretRepeatNode extends RepeatNode implements InterpretControlNode, InterpretLocality {
    public InterpretControlResult interpretControl(boolean previousSuccess, boolean previousFailure, InterpretValue previousValue) {
        InterpretResult maybeRepetitionsResult = getFirstInterpretNode().interpretValue();
        if(!maybeRepetitionsResult.isValue()) throw getFirst().runtimeError("Unexpected return in condition for repeat operator");

        InterpretValue maybeRepetitions = maybeRepetitionsResult.asValue();
        if(maybeRepetitions instanceof InterpretInt repetitions) {
            int numRepetitions = repetitions.getValue();
            InterpretSequence ret = new InterpretSequence(getSecond().getType());

            for(int i = 0; i < numRepetitions; i++) {
                initVariables();
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
            throw getFirst().syntaxError("Repeat operator requires a number for the number of repetitions");
        }
    }

    @Override
    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot assign to result of repeat loop");
    }
}
