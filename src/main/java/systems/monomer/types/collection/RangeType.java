package systems.monomer.types.collection;


import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.values.InterpretRange;
import systems.monomer.types.Type;

public class RangeType extends CollectionType {
    public RangeType(Type elementType) {
        super(elementType);
    }

    @Override
    public Type indexResult(Type indexType) {
        return getElementType();
    }

    @Override
    public InterpretValue defaultValue() {
        InterpretValue defaultElementValue = getElementType().defaultValue();
        InterpretRange ret = new InterpretRange(getElementType(), true, true);
        ret.setStart(defaultElementValue);
        ret.setStop(defaultElementValue);
        ret.setStep(defaultElementValue);

        return ret;
    }

    @Override
    public int serial() {
        return super.serial() + 0x00800000;
    }
}
