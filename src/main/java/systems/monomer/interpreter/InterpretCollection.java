package systems.monomer.interpreter;

import lombok.Getter;
import systems.monomer.types.CollectionType;
import systems.monomer.types.Type;

import java.util.Collection;

@Getter
public abstract class InterpretCollection extends CollectionType implements InterpretValue {
    public InterpretCollection(Type elementType) {
        super(elementType);
    }

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

    public InterpretCollection clone() {
        InterpretCollection cloned = (InterpretCollection) super.clone();
        throw new Error("TODO unimplemented");
    }
}
