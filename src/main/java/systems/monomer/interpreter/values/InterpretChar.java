package systems.monomer.interpreter.values;

import lombok.Getter;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.primitive.CharType;

@Getter
public final class InterpretChar extends CharType implements InterpretValue {
    private final Character value;
    public InterpretChar(char value) {
        this.value = value;
    }

    public String valueString() {
        return String.valueOf(value);
    }

    public InterpretChar clone() {
        return new InterpretChar(value);
    }
}
