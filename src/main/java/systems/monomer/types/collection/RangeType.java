package systems.monomer.types.collection;

import interpret.InterpretValue;
import interpret.InterpretValueA;
import types.Type;

//TODO
public class RangeType extends CollectionType {
    public RangeType() {
    }

    public RangeType(Type elementType) {
        super(elementType);
    }

    @Override
    public InterpretValue defaultValue() {
        return new InterpretValueA();
    }

    @Override
    public int serial() {
        return super.serial() + 0x00800000;
    }
}
