package systems.monomer.types.primitive;


import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.values.InterpretBool;

public class BoolType extends PrimativeType<InterpretBool> {
    public static final BoolType BOOL = new BoolType();

    @Override
    public int serial() {
        return super.serial() + 0x00040000;
    }

    @Override
    public InterpretBool defaultValue() {
        return InterpretBool.FALSE;
    }

    @Override
    public String valueString() {
        return "BOOL";
    }
}
