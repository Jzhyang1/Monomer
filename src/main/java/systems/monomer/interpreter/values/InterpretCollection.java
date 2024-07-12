package systems.monomer.interpreter.values;

import lombok.Getter;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.collection.CollectionType;
import systems.monomer.types.Type;

import java.util.Collection;
import java.util.Iterator;

public interface InterpretCollection extends InterpretValue {

    public abstract Collection<? extends InterpretValue> getValues();

    default Iterator<? extends InterpretValue> iterator() {
        return getValues().iterator();
    }

    default int size() {
        return getValues().size();
    }

    public abstract void add(InterpretValue value);

    default String valueString() {
        return getValues().stream()
                .map(InterpretValue::valueString)
                .reduce((a, b) -> a + "," + b)
                .orElse("");
    }
}
