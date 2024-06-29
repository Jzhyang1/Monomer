package systems.monomer.types;

import systems.monomer.interpreter.values.InterpretSequence;
import systems.monomer.interpreter.InterpretValue;

public class SequenceType extends CollectionType {
    public static final SequenceType SEQUENCE = new SequenceType(new AnyType());
    public static boolean isSequence(Type type) {
        return type instanceof SequenceType || type instanceof InterpretSequence;   //TODO remove instanceof
    }

    public SequenceType(Type elementType) {
        super(elementType);
    }

    @Override
    public SequenceType clone() {
        return (SequenceType) super.clone();
    }

    @Override
    public String valueString() {
        return "sequence{" + getElementType().valueString() + "}";
    }

    @Override
    public boolean typeContains(Type type) {
        return type instanceof SequenceType sequence && getElementType().typeContains(sequence.getElementType());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SequenceType sequence && getElementType().equals(sequence.getElementType());
    }

    @Override
    public InterpretValue defaultValue() {
        return new InterpretSequence(getElementType());
    }
}
