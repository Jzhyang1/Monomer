package systems.monomer.interpreter;

import systems.monomer.syntaxtree.VariableNode;

public class InterpretVariableNode extends VariableNode implements InterpretNode {

    public InterpretVariableNode(String name) {
        super(name);
    }

    public InterpretVariable interpretVariable() {
        return variableKey;
    }

    public InterpretValue interpretValue() {
        return variableKey.getValue();
    }

}
