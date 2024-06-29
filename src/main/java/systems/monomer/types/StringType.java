package systems.monomer.types;

import systems.monomer.interpreter.values.InterpretString;
import systems.monomer.interpreter.InterpretValue;

/**
 * The type of a string.
 * In assembly, the memory layout of a string is:
 * 8 bytes: the size of the string
 * 8 bytes: the pointer to the string
 */

public class StringType extends ListType {
    public static final StringType STRING = new StringType();

    public StringType() {
        super(CharType.CHAR);
    }

    @Override
    public String valueString() {
        return "str";
    }

    @Override
    public StringType clone(){
        return (StringType) super.clone();
    }

    @Override
    public boolean typeContains(Type type) {
        return type instanceof StringType;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof StringType;
    }

    @Override
    public InterpretValue defaultValue() {
        return new InterpretString("");
    }
}
