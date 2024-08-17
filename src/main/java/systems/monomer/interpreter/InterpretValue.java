package systems.monomer.interpreter;

import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.types.Type;

import static systems.monomer.errorhandling.ErrorBlock.programError;

public interface InterpretValue extends Type, InterpretResult {
    @Override
    default InterpretValue asValue() {
        return this;
    }

    default InterpretValue call(InterpretValue args, InterpretValue namedArgs) {
        throw programError(this + " is not a function", ErrorBlock.Reason.RUNTIME);
    }

    public default <T> T getValue() {
        throw programError(this + " is not a raw value", ErrorBlock.Reason.RUNTIME);
    }

    public default InterpretValue getField(String name) {
        throw programError("Can not assign to " + this, ErrorBlock.Reason.SYNTAX);
    }

    public int compareValueTo(InterpretValue other);

    public InterpretValue clone();
}
