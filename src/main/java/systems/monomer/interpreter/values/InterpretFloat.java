package systems.monomer.interpreter.values;

import lombok.Getter;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.primitive.FloatType;

@Getter
public final class InterpretFloat extends FloatType implements InterpretValue {
    private final Double value;

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

    @Override
    public int compareValueTo(InterpretValue maybeo) {
        if(!(maybeo instanceof InterpretFloat o)) return compareTo(maybeo);

        return Double.compare(value, o.value);
    }
}
