package systems.monomer.types.primitive;

import systems.monomer.interpreter.values.InterpretChar;

public class CharType extends PrimativeType<InterpretChar> {
    public static final CharType CHAR = new CharType();

    @Override
    public int serial() {
        return super.serial() + 0x00030000;
    }

    @Override
    public InterpretChar defaultValue() {
        return new InterpretChar('\0');
    }

    @Override
    public String valueString() {
        return "CHAR";
    }
}
