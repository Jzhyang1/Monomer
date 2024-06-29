package systems.monomer.interpreter.values;

import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.BoolType;

public class InterpretBool extends BoolType implements InterpretValue {
    public static final InterpretValue FALSE = new InterpretBool(false);
    public static final InterpretValue TRUE = new InterpretBool(true);

    private final boolean value;

    public InterpretBool(boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }

    @Override
    public InterpretValue defaultValue() {
        return this;
    }

    public String valueString() {
        return Boolean.toString(value);
    }

    public InterpretBool clone() {
        return (InterpretBool) super.clone();
    }
}
