package systems.monomer.types.collection;


import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.values.InterpretList;
import systems.monomer.types.Type;
import systems.monomer.types.primitive.IntType;

import static systems.monomer.errorhandling.ErrorBlock.programError;

public class ListType extends CollectionType {
    public static final ListType LIST = new ListType();

    public ListType() {
    }

    public ListType(Type inner) {
        super(inner);
    }

    @Override
    public Type indexResult(Type indexType) {
        if(!IntType.INT.typeContains(indexType)) {
            throw programError("List requires int as index but instead received " + indexType, ErrorBlock.Reason.SYNTAX);
        }
        return getElementType();
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
