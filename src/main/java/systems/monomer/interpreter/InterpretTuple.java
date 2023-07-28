package systems.monomer.interpreter;

import java.util.Collection;

public class InterpretTuple extends InterpretCollectionValue {
    protected Collection<InterpretValue> getValues() {
        throw new Error("TODO unimplemented");
    }

    public InterpretTuple clone() {
        throw new Error("TODO unimplemented");
    }
}
