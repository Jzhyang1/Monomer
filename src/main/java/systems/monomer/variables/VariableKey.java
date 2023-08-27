package systems.monomer.variables;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.types.Type;

@Getter @Setter
public class VariableKey extends InterpretVariable {
    private VariableKey parent;
    private InterpretValue value;
    private Type type;

    public VariableKey(){}
    public VariableKey(VariableKey parent) {
        this.parent = parent;
    }

    public boolean isMultivar() {
        return false;   //TODO
    }

    public void put(String field, Type value) {
        setField(field, value);   //uses the Type class's setField to store the value as a field
        if(value instanceof VariableKey key)
            key.setParent(this);
        else
            throw new Error("TODO unimplemented");  //TODO throwError
    }

    public VariableKey get(String field) {
        return (VariableKey) getField(field); //TODO check and throwError
    }

    @Override
    public InterpretValue call(InterpretValue args) {
        return super.call(args);
    }

    public @Nullable InterpretValue getValue() {
        return value;
    }

    public String valueString() {
        return value == null ? super.valueString() : value.valueString();
    }

    @Override
    public boolean typeContains(Type type) {
        return false;
    }

    @Override
    public boolean hasField(String field) {
        return super.hasField(field);
    }

    @Override
    public void assertField(String field, Type value) {
        super.assertField(field, value);
    }

    @Override
    public Type getField(String field) {
        return super.getField(field);
    }
}
