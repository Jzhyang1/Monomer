package systems.monomer.interpreter.values;

import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.primitive.FloatType;

public final class InterpretFloat extends FloatType implements InterpretValue {
    private final double value;

    public InterpretFloat(double value) {
        this.value = value;
    }

    @Override
    public String valueString() {
        return String.valueOf(value);
    }

    @Override
    public InterpretFloat clone() {
        return new InterpretFloat(value);
    }
}
