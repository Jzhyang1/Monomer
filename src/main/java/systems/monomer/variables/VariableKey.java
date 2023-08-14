package systems.monomer.variables;

import lombok.Getter;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;

@Getter
public class VariableKey extends InterpretVariable {
    private VariableKey parent;
    private InterpretValue value;

    public VariableKey(){}
    public VariableKey(VariableKey parent) {
        this.parent = parent;
    }

    public void setParent(VariableKey parent) {
        this.parent = parent;
    }

    public void setValue(InterpretValue value) {
        this.value = value;
    }

    public boolean isMultivar() {
        return false;   //TODO
    }
}
