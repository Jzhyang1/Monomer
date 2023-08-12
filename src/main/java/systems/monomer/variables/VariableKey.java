package systems.monomer.variables;

import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;

public class VariableKey extends InterpretVariable {
    private VariableKey parent;
    private InterpretValue value;

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

    public InterpretValue getValue() {
        return value;
    }
    public void setValue(InterpretValue value) {
        this.value = value;
    }

    public boolean isMultivar() {
        return false;   //TODO
    }
}
