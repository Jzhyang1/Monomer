package systems.monomer.interpreter;

import systems.monomer.variables.Type;

public abstract class InterpretValue extends Type {
    public void put(String field, InterpretValue value) {
        super.put(field, value);
    }
    public InterpretValue get(String field) {
        return (InterpretValue) super.get(field);
    }

    public boolean typeContains(InterpretValue type) {
        throw new Error("TODO unimplemented");
    }

    public abstract String valueString();
}
