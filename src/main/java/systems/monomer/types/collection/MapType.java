package systems.monomer.types.collection;


import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.values.InterpretMap;
import systems.monomer.types.Type;
import systems.monomer.types.tuple.TupleType;

import java.util.List;

public class MapType extends SetType {
    public static class EntryType extends TupleType {
        public EntryType(Type key, Type value) {
            super(List.of(key, value));
        }
    }

    public MapType(Type key, Type value) {
        super(new EntryType(key, value));
    }
    public MapType(TupleType entry) {
        super(entry);
    }

    @Override
    public InterpretValue defaultValue() {
        return new InterpretMap((TupleType) getElementType());
    }

    @Override
    public int serial() {
        return super.serial() + 0x00030000;
    }
}
