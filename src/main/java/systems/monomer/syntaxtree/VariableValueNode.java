package systems.monomer.syntaxtree;

import systems.monomer.variables.VariableKey;

public class VariableValueNode extends VariableNode {
    public VariableValueNode(String name) {
        super(name);
    }
    public VariableValueNode(String name, VariableKey key) {
        super(name, key);
    }

    //no matchVariables() needed

    public Node matchTypes() {
        setType(variableKey.getType().getExpressed());
        return this;
    }

    @Override
    public Node simplify() {
        //in case type has updated in the key
        setType(variableKey.getType().getExpressed());
        return this;
    }
}
