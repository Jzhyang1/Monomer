package systems.monomer.types;

public class CharType extends AnyType {
    public static final CharType CHAR = new CharType();

    @Override
    public String valueString() {
        return "char";
    }

    @Override
    public CharType clone(){
        return (CharType) super.clone();
    }

    @Override
    public boolean typeContains(Type type) {
        return type instanceof CharType;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CharType;
    }
}
