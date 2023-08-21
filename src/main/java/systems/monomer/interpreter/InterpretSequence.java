package systems.monomer.interpreter;

import systems.monomer.types.Type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InterpretSequence extends InterpretCollectionValue{
    public InterpretSequence(){}
    public InterpretSequence(Type type) {
        setType(type);
    }

    private List<InterpretValue> values = new ArrayList<>();
    public Collection<InterpretValue> getValues() {
        return values;
    }

    public InterpretSequence clone() {
        throw new Error("TODO unimplemented");
    }
}
