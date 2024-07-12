package systems.monomer.interpreter.values;

import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.collection.MapType;
import systems.monomer.types.Type;
import systems.monomer.types.tuple.TupleType;

import java.util.List;

public class InterpretMap extends MapType implements InterpretValue {
    public InterpretMap(Type elementType, Type valueType) {
        super(elementType, valueType);
    }
    public InterpretMap(TupleType pair) {
        super(pair);
    }

    public String valueString() {
        throw new Error("TODO unimplemented");
    }

    public InterpretMap clone() {
        throw new Error("TODO unimplemented");
    }
}
