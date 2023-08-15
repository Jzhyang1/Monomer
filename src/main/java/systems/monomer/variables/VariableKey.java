package systems.monomer.variables;

import lombok.Getter;
import lombok.Setter;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;

@Getter @Setter
public class VariableKey extends InterpretVariable {
    private VariableKey parent;
    private InterpretValue value;

    public VariableKey(){}
    public VariableKey(VariableKey parent) {
        this.parent = parent;
    }

    public boolean isMultivar() {
        return false;   //TODO
    }

    @Override
    public void put(String field, Type value) {
        super.put(field, value);
        if(value instanceof VariableKey key)
            key.setParent(this);
        else
            throw new Error("TODO unimplemented");  //TODO throwError
    }

    public VariableKey get(String field) {
        return (VariableKey)super.get(field); //TODO check and throwError
    }

    public InterpretValue getValue() {
        return value == null ? this : value;
    }

    public String valueString() {
        return value == null ? super.valueString() : value.valueString();
    }
}
