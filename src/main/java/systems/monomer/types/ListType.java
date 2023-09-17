package systems.monomer.types;

import systems.monomer.interpreter.InterpretList;
import systems.monomer.interpreter.InterpretTuple;
import systems.monomer.interpreter.InterpretValue;

public class ListType extends CollectionType {
    public static final ListType LIST = new ListType(AnyType.ANY);

    public ListType(Type type) {
        super(type);
    }

    @Override
    public InterpretValue defaultValue() {
        return new InterpretList(getElementType());
    }
}
