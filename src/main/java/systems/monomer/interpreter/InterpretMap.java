package systems.monomer.interpreter;

import systems.monomer.types.MapType;
import systems.monomer.types.Type;

public class InterpretMap extends MapType implements InterpretValue {
    public InterpretMap(Type elementType, Type valueType) {
        super(elementType, valueType);
    }

    public String valueString() {
        throw new Error("TODO unimplemented");
    }

    public InterpretMap clone() {
        throw new Error("TODO unimplemented");
    }
}
