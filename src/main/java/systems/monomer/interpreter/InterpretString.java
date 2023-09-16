package systems.monomer.interpreter;

import lombok.Getter;
import systems.monomer.types.StringType;

@Getter
public class InterpretString extends StringType implements InterpretValue {
    private final String value;
    public InterpretString(String value) {
        this.value = value;
    }

    public String valueString() {
        return value;
    }

    @Override
    public InterpretString clone() {
        return (InterpretString) super.clone();
    }
}
