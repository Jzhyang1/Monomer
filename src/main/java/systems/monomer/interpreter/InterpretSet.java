package systems.monomer.interpreter;

import java.util.Collection;

public class InterpretSet extends InterpretCollectionValue{
    protected Collection<InterpretValue> getValues() {
        throw new Error("TODO unimplemented");
    }

    public InterpretValue copy() {
        throw new Error("TODO unimplemented");
    }
}
