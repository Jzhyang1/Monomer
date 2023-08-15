package systems.monomer.interpreter;

import systems.monomer.variables.Type;

public abstract class InterpretValue extends Type {
    public InterpretValue get(String field) {
        return (InterpretValue) super.get(field);
    }

    public boolean typeContains(InterpretValue type) {
        throw new Error("TODO unimplemented");
    }
}
