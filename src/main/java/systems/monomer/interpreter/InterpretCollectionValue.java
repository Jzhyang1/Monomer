package systems.monomer.interpreter;

import java.util.Collection;

public abstract class InterpretCollectionValue extends InterpretValue {
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
