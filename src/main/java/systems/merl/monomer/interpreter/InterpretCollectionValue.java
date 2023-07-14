package systems.merl.monomer.interpreter;

import java.util.Collection;

public abstract class InterpretCollectionValue extends InterpretValue {
    protected abstract Collection<InterpretValue> getValues();

    public void add(InterpretValue value) {
        getValues().add(value);
    }
    public String valueString() {
        return getValues().toString();
    }
}
