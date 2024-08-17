package systems.monomer.types.collection;


import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.values.InterpretMap;
import systems.monomer.types.Type;
import systems.monomer.types.tuple.TupleType;

import java.util.List;

import static systems.monomer.errorhandling.ErrorBlock.programError;

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
    public Type indexResult(Type indexType) {
        TupleType entry = (TupleType) getElementType();
        if (!entry.get(0).typeContains(indexType))
            throw programError("Map requires key type but instead received " + indexType, ErrorBlock.Reason.SYNTAX);
        return entry.get(1);
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
