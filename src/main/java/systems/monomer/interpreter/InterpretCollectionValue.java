package systems.monomer.interpreter;

import lombok.Getter;
import lombok.Setter;
import systems.monomer.variables.Type;

import java.util.Collection;

public abstract class InterpretCollectionValue extends InterpretValue {
    @Getter @Setter
    private Type type;

    public abstract Collection<InterpretValue> getValues();

    public void add(InterpretValue value) {
        getValues().add(value);
    }
    public String valueString() {
        return getValues().stream()
                .map(InterpretValue::valueString)
                .reduce((a, b) -> a + "," + b)
                .orElse("");
    }

    public InterpretCollectionValue clone() {
        InterpretCollectionValue cloned = (InterpretCollectionValue) super.clone();
        throw new Error("TODO unimplemented");
    }
}
