package systems.monomer.interpreter.operators;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.interpreter.values.InterpretTuple;
import systems.monomer.syntaxtree.operators.AssignNode;

public class InterpretAssignNode extends AssignNode implements InterpretNode {
    public InterpretVariable interpretVariable() {
        InterpretNode first = getFirstInterpretNode();

        return first.interpretVariable();
    }

    public InterpretResult interpretValue() {
        if(functionInit != null)
            return InterpretTuple.EMPTY;

        InterpretNode valueNode = getInterpretNode(size() - 1);
        InterpretResult valueResult = valueNode.interpretValue();
        if(!valueResult.isValue())
            return valueResult;

        InterpretValue ret = valueResult.asValue();
        for (int i = size() - 2; i >= 0; --i) {
            InterpretNode destinationNode = getInterpretNode(i);
            destinationNode.interpretAssign(ret, toLock);
        }

        return ret;
    }
}
