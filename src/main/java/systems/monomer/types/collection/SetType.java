package systems.monomer.types.collection;


import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.values.InterpretSet;
import systems.monomer.types.Type;

public class SetType extends CollectionType {
    public SetType() {
    }

    public SetType(Type elementType) {
        super(elementType);
    }

    @Override
    public InterpretValue defaultValue() {
        return new InterpretSet(getElementType());
    }

    @Override
    public int serial() {
        return super.serial() + 0x00020000;
    }
}
