package systems.monomer.interpreter.values;

import lombok.Getter;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.plural.StringType;

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
