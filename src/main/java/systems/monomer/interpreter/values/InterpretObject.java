package systems.monomer.interpreter.values;

import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.object.ObjectType;
import systems.monomer.util.Util;

import java.util.List;

import static systems.monomer.errorhandling.ErrorBlock.programError;

public final class InterpretObject extends ObjectType implements InterpretValue {
    public static final InterpretValue EMPTY = new InterpretObject();

    public InterpretObject clone() {
        try {
            return (InterpretObject) super.clone();
        } catch (CloneNotSupportedException e) {
            throw programError("Unable to clone " + this.valueString(), ErrorBlock.Reason.OTHER);
        }
    }

    public void set(String key, InterpretValue interpretValue) {
        setField(key, interpretValue);
    }

    public InterpretValue getField(String key) {
        return (InterpretValue) super.getField(key);
    }

    @Override
    public int compareValueTo(InterpretValue other) {
        if (!(other instanceof InterpretObject otherObject)) {
            return compareTo(other);
        }

        List<String> thisKeys = getSortedKeys();
        List<String> otherKeys = otherObject.getSortedKeys();

        if(thisKeys.size() != otherKeys.size()) {
            return thisKeys.size() - otherKeys.size();
        }

        int keyComparison = Util.pairCheck(thisKeys, otherKeys, (a, b) -> a.compareTo(b), 0);
        return keyComparison;
    }
}
