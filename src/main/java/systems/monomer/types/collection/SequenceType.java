package systems.monomer.types.collection;


import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.values.InterpretSequence;
import systems.monomer.types.Type;

public class SequenceType extends CollectionType {
    public static final SequenceType SEQUENCE = new SequenceType();

    public SequenceType() {
    }

    public SequenceType(Type elementType) {
        super(elementType);
    }


    @Override
    public InterpretValue defaultValue() {
        return new InterpretSequence(getElementType());
    }

    @Override
    public int serial() {
        return super.serial() + 0x00010000;
    }
}
