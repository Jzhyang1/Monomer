package systems.monomer.types;

import systems.monomer.interpreter.InterpretString;
import systems.monomer.interpreter.InterpretValue;

public class StringType extends ObjectType {
    public static final StringType STRING = new StringType();

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
