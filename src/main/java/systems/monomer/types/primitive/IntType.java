package systems.monomer.types.primitive;


import systems.monomer.interpreter.values.InterpretInt;

public class IntType extends PrimativeType<InterpretInt> {
    public static final IntType INT = new IntType();

    @Override
    public int serial() {
        return super.serial() + 0x00020000;
    }

    @Override
    public InterpretInt defaultValue() {
        return new InterpretInt(0);
    }

    @Override
    public String valueString() {
        return "INT";
    }
}
