package systems.monomer.types.primitive;


import systems.monomer.interpreter.values.InterpretFloat;

public class FloatType extends PrimativeType<InterpretFloat> {
    public static final FloatType FLOAT = new FloatType();

    @Override
    public int serial() {
        return super.serial() + 0x00010000;
    }

    @Override
    public InterpretFloat defaultValue() {
        return new InterpretFloat(0);
    }

    @Override
    public String valueString() {
        return "FLOAT";
    }
}
