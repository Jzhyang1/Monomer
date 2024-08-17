package systems.monomer.types.collection;


import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.values.InterpretSet;
import systems.monomer.types.Type;
import systems.monomer.types.primitive.BoolType;

import static systems.monomer.errorhandling.ErrorBlock.programError;

public class SetType extends CollectionType {
    public SetType() {
    }

    public SetType(Type elementType) {
        super(elementType);
    }

    @Override
    public Type indexResult(Type indexType) {
        if(!indexType.typeContains(getElementType())) {
            throw programError("Set requires element type as index but instead received " + indexType, ErrorBlock.Reason.SYNTAX);
        }
        return BoolType.BOOL;
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
