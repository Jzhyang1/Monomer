package systems.monomer.types.pseudo;


import systems.monomer.types.Type;

public class AnyType extends PseudoType {
    public static final AnyType ANY = new AnyType();

    @Override
    public boolean typeContains(Type other) {
        return true;
    }

    @Override
    public int serial() {
        return 0;
    }

    @Override
    public Type simplify() {
        //t == ANY includes when a PlaceholderType is not replaced before simplification
        throw new RuntimeException("Unresolved ANY type");
    }

    @Override
    public String valueString() {
        return "ANY";
    }
}
