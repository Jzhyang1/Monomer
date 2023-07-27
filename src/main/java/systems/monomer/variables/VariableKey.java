package systems.monomer.variables;

import systems.monomer.interpreter.InterpretVariable;

public class VariableKey extends InterpretVariable {
    private VariableKey parent;

    public VariableKey(){}
    public VariableKey(VariableKey parent) {
        this.parent = parent;
    }

    public VariableKey getParent() {
        return parent;
    }

    public void setParent(VariableKey parent) {
        this.parent = parent;
    }
}
