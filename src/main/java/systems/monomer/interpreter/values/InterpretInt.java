package systems.monomer.interpreter.values;

import lombok.Getter;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.primitive.IntType;

@Getter
public final class InterpretInt extends IntType implements InterpretValue {
    private final Integer value;

    public InterpretInt(int value) {
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

    @Override
    public int compareValueTo(InterpretValue maybeo) {
        if(!(maybeo instanceof InterpretInt o)) return compareTo(maybeo);

        return Long.compare(value, o.value);
    }
}
