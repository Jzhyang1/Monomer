package systems.monomer.interpreter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InterpretSequence extends InterpretCollectionValue{
    private List<InterpretValue> values = new ArrayList<>();
    public Collection<InterpretValue> getValues() {
        return values;
    }

    public InterpretSequence clone() {
        throw new Error("TODO unimplemented");
    }
}
