package systems.monomer.interpreter;

import systems.monomer.interpreter.variables.InterpretKey;
import systems.monomer.interpreter.variables.InterpretVariable;
import systems.monomer.syntaxtree.VariableNode;

public class InterpretVariableNode extends VariableNode implements InterpretNode {

    public InterpretVariableNode(String name) {
        super(name);
    }

    public InterpretVariable interpretVariable() {
        return (InterpretVariable) variableKey;
    }

    public InterpretValue interpretValue() {
        return ((InterpretVariable) variableKey).getValue();
    }

}
