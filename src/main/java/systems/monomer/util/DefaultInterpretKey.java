package systems.monomer.util;

import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.variables.InterpretKey;
import systems.monomer.types.Type;

public final class DefaultInterpretKey extends InterpretKey {
    public DefaultInterpretKey(InterpretValue value, Type type) {
        setValue(value);
        setType(type);
    }
}