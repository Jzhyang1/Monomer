package systems.monomer.interpreter;

import lombok.Getter;
import systems.monomer.types.CollectionType;
import systems.monomer.types.Type;

import java.util.Collection;
import java.util.Iterator;

@Getter
public abstract class InterpretCollection extends CollectionType implements InterpretValue {
    public InterpretCollection(Type elementType) {
        super(elementType);
    }

    public abstract Collection<? extends InterpretValue> getValues();

    public Iterator<? extends InterpretValue> iterator() {
        return getValues().iterator();
    }

    public int size() {
        return getValues().size();
    }

    public abstract void add(InterpretValue value);

    public String valueString() {
        return getValues().stream()
                .map(InterpretValue::valueString)
                .reduce((a, b) -> a + "," + b)
                .orElse("");
    }

    public InterpretCollection clone() {
        InterpretCollection cloned = (InterpretCollection) super.clone();
        throw new Error("TODO unimplemented");
    }
}
