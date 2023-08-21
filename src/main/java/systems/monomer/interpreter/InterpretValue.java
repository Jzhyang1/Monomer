package systems.monomer.interpreter;

import systems.monomer.types.Type;

public abstract class InterpretValue extends Type {
    public InterpretValue get(String field) {
        return (InterpretValue) super.get(field);
    }

    public InterpretValue call(InterpretValue args) {
        throw new Error(this + " is not a function");
    }

    public boolean typeContains(InterpretValue type) {
        throw new Error("TODO unimplemented");
    }
}
