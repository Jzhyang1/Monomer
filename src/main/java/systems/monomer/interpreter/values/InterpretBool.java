package systems.monomer.interpreter.values;

import lombok.Getter;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.primitive.BoolType;

import static systems.monomer.util.Util.boolAsInt;

@Getter
public final class InterpretBool extends BoolType implements InterpretValue {
    public static final InterpretBool FALSE = new InterpretBool(false);
    public static final InterpretBool TRUE = new InterpretBool(true);

    private final Boolean value;

    public InterpretBool(boolean value) {
        this.value = value;
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

    public int compareValueTo(InterpretValue maybeo) {
        if(!(maybeo instanceof InterpretBool o)) return compareTo(maybeo);

        return Boolean.compare(value, o.value);
    }
}
