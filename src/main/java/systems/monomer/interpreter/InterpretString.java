package systems.monomer.interpreter;

import lombok.Getter;
import systems.monomer.types.StringType;

@Getter
public class InterpretString extends StringType implements InterpretValue {
    private String value;
    public InterpretString(String value) {
        this.value = value;
    }

    public String valueString() {
        return value;
    }
}
