package systems.monomer.interpreter;

import systems.monomer.types.Type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InterpretSequence extends InterpretCollection {
    private final List<InterpretValue> values = new ArrayList<>();

    public InterpretSequence(Type elementType) {
        super(elementType);
    }

    public Collection<InterpretValue> getValues() {
        return values;
    }

    public void add(InterpretValue value) {
        values.add(value);
    }

    public InterpretSequence clone() {
        throw new Error("TODO unimplemented");
    }
}
