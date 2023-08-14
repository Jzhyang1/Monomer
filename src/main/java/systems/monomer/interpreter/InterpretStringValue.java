package systems.monomer.interpreter;

import lombok.Getter;

@Getter
public class InterpretStringValue extends InterpretValue {
    private String value;
    public InterpretStringValue(String value) {
        this.value = value;
    }

    public String valueString() {
        return value;
    }
}
