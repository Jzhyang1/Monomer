package systems.monomer.interpreter.values;

import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.primitive.IntType;

public final class InterpretInt extends IntType implements InterpretValue {
    private final long value;

    public InterpretInt(long value) {
        this.value = value;
    }

    @Override
    public String valueString() {
        return String.valueOf(value);
    }

    @Override
    public InterpretInt clone() {
        return new InterpretInt(value);
    }
}
