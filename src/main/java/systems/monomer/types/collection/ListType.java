package systems.monomer.types.collection;


import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.values.InterpretList;
import systems.monomer.types.Type;

public class ListType extends CollectionType {
    public static final ListType LIST = new ListType();

    public ListType() {
    }

    public ListType(Type inner) {
        super(inner);
    }

    @Override
    public InterpretValue defaultValue() {
        return new InterpretList(getElementType());
    }

    @Override
    public int serial() {
        return super.serial() + 0x00040000;
    }
}
