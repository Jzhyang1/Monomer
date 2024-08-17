package systems.monomer.types.collection;

import systems.monomer.interpreter.values.InterpretString;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.Type;
import systems.monomer.types.primitive.CharType;

import java.util.Map;

public class StringType extends ListType {
    public static final StringType STRING = new StringType();
    
    public StringType() {
        super(CharType.CHAR);
    }

    @Override
    public Type testReplace(Map<Type, Type> replacements) {
        return this;
    }

    @Override
    public InterpretValue defaultValue() {
        return new InterpretString("");
    }

    @Override
    public int serial() {
        return super.serial() + 0x00100000;
    }

    @Override
    public String valueString() {
        return "STR";
    }
}
