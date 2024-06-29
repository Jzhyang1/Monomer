package systems.monomer.interpreter.values;

import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.Type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//TODO make this a SequenceType
public class InterpretSequence extends InterpretCollection {
    private final List<InterpretValue> values = new ArrayList<>();

    public InterpretSequence(Type elementType) {
        super(elementType);
    }

    public InterpretSequence(Collection<? extends InterpretValue> list) {
        //TODO set the type to the most general type
        super(list.iterator().next());
        values.addAll(list);
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
