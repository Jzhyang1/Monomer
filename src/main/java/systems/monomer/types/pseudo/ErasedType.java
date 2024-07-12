package systems.monomer.types.pseudo;


import systems.monomer.types.Type;

public class ErasedType extends PseudoType {
    public static ReturnType ERASED = new ReturnType();

    @Override
    public Type simplify() {
        throw new RuntimeException("ErasedType not erased");
    }

    @Override
    public int serial() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String valueString() {
        return "ERASED";
    }
}
