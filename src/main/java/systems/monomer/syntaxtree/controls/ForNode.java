package systems.monomer.syntaxtree.controls;
import systems.monomer.interpreter.*;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.operators.OperatorNode;
import systems.monomer.types.AnyType;
import systems.monomer.types.CollectionType;
import systems.monomer.types.Type;
import systems.monomer.variables.Key;
import systems.monomer.variables.VariableKey;

import java.util.Iterator;

public class ForNode extends ControlOperatorNode {
    private Key iteratorKey;

    public ForNode() {
        super("for");
    }

    public void matchTypes() {
        OperatorNode firstNode = (OperatorNode) getFirst();
        firstNode.matchTypes();
        Node vars = firstNode.getFirst();
        Node collection = firstNode.getSecond();

        Type maybeCollectionType = collection.getType();
        if(maybeCollectionType instanceof CollectionType collectionType) {
            vars.setType(collectionType.getElementType());  //TODO handle multiple vars
            System.out.println(vars.getType());
        } else {
            throwError("For loop requires a collection for the control of repetitions");
        }

        Node secondNode = getSecond();
        secondNode.matchTypes();
        setType(new InterpretSequence(secondNode.getType()));
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

        //TODO unchecked asValue
        InterpretValue maybeIterable = collection.interpretValue().asValue();

        if(maybeIterable instanceof InterpretCollection iterable) {
            Iterator<InterpretValue> iter = iterable.getValues().iterator();
            InterpretSequence ret = new InterpretSequence(iterable.getElementType());

            while(iter.hasNext()) {
                InterpretValue val = iter.next();
                iteratorKey.setValue(val);

                //TODO set iterator variable within the Monomer loop
                InterpretResult result = getSecond().interpretValue();

                if(result.isValue()) {
                    ret.add(result.asValue());
                }
                else if(result instanceof InterpretBreaking breaking) {
                    if("continue".equals(breaking.getName())) continue;
                    else if("break".equals(breaking.getName()))
                        return new InterpretControlResult(false, result.asValue());
                    return new InterpretControlResult(false, result);
                }
            }
            return new InterpretControlResult(true, ret);
        } else {
            getFirst().throwError("For operator requires a collection for the control of repetitions");
            return null;
        }
    }
}
