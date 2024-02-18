package systems.monomer.interpreter;

import systems.monomer.types.Type;

public interface InterpretValue extends Type, InterpretResult {
    @Override
    default InterpretValue asValue() {
        return this;
    }

    default InterpretValue get(String field) {
        throw new Error("TODO unimplemented");
    }

    default InterpretValue call(InterpretValue args, InterpretValue namedArgs) {
        throw new Error(this + " is not a function");
    }

    public default <T> T getValue() {
        throw new Error(this + " is not a raw value");
    }

    public InterpretValue clone();
}
