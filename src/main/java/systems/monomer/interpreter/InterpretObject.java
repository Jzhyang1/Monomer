package systems.monomer.interpreter;

import systems.monomer.types.ObjectType;

public class InterpretObject extends ObjectType implements InterpretValue {
    public InterpretObject clone() {
        return (InterpretObject) super.clone();
    }

    public void set(String key, InterpretValue interpretValue) {
        setField(key, interpretValue);
    }

    public InterpretValue get(String key) {
        return (InterpretValue) getField(key);
    }
}
