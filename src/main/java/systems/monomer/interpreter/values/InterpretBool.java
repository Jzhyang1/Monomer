package systems.monomer.interpreter.values;

import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.primitive.BoolType;

public final class InterpretBool extends BoolType implements InterpretValue {
    public static final InterpretBool FALSE = new InterpretBool(false);
    public static final InterpretBool TRUE = new InterpretBool(true);

    private final boolean value;

    public InterpretBool(boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }

    @Override
    public InterpretBool defaultValue() {
        return this;
    }

    public String valueString() {
        return Boolean.toString(value);
    }

    public InterpretBool clone() {
        return new InterpretBool(value);
    }
}
