package systems.monomer.interpreter;

import lombok.Getter;
import systems.monomer.types.CharType;

@Getter
public class InterpretChar extends CharType implements InterpretValue {
    private final Character value;
    public InterpretChar(char value) {
        this.value = value;
    }

    public String valueString() {
        return String.valueOf(value);
    }

    public InterpretChar clone() {
        return (InterpretChar) super.clone();
    }
}
