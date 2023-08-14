package systems.monomer.interpreter;

import lombok.Getter;

@Getter
public class InterpretCharValue extends InterpretValue {
    private char value;
    public InterpretCharValue(char value) {
        this.value = value;
    }

    public String valueString() {
        return String.valueOf(value);
    }
}
