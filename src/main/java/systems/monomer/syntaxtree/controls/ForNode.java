package systems.monomer.syntaxtree.controls;
import systems.monomer.interpreter.InterpretCollection;
import systems.monomer.interpreter.InterpretSequence;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.operators.OperatorNode;
import systems.monomer.types.AnyType;
import systems.monomer.types.CollectionType;
import systems.monomer.types.Type;
import systems.monomer.variables.VariableKey;

import java.util.Iterator;

public class ForNode extends ControlOperatorNode {
    private VariableKey iteratorKey;

    public ForNode() {
        super("for");
    }

    public void matchTypes() {
        super.matchTypes();

        OperatorNode firstNode = (OperatorNode) getFirst();
        Node vars = firstNode.getFirst();
        Node collection = firstNode.getSecond();

        Type maybeCollectionType = collection.getType();
        if(maybeCollectionType instanceof CollectionType collectionType) {
            vars.setType(collectionType.getElementType());  //TODO handle multiple vars
        } else {
            throwError("For loop requires a collection for the control of repetitions");
        }
        setType(new InterpretSequence(getType()));
    }

    public void matchVariables() {
        super.matchVariables();

        Node firstNode = getFirst();
        if(firstNode.getUsage() != Usage.OPERATOR &&
                !"in".equals(firstNode.getName()))
            throwError("For loop should be formatted as 'for <variable> in <collection>'");
        iteratorKey = ((OperatorNode) firstNode).getFirst().getVariableKey();
    }

    public InterpretControlResult interpretControl(boolean previousSuccess, boolean previousFailure, InterpretValue previousValue) {
        OperatorNode firstNode = (OperatorNode) getFirst();
        Node collection = firstNode.getSecond();

        InterpretValue maybeIterable = collection.interpretValue();
        if(maybeIterable instanceof InterpretCollection iterable) {
            Iterator<InterpretValue> iter = iterable.getValues().iterator();
            InterpretSequence ret = new InterpretSequence(AnyType.ANY); //TODO find the actual type

            while(iter.hasNext()) {
                InterpretValue val = iter.next();
                iteratorKey.setValue(val);

                //TODO set iterator variable within the Monomer loop
                InterpretValue result = getSecond().interpretValue();
                //TODO handle break
                ret.add(result);
            }
            return new InterpretControlResult(true, ret);
        } else {
            getFirst().throwError("For operator requires a collection for the control of repetitions");
            return null;
        }
    }
}
