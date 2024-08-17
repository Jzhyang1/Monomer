package systems.monomer.interpreter.values;

import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.Type;
import systems.monomer.types.collection.SetType;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static systems.monomer.errorhandling.ErrorBlock.programError;

public final class InterpretSet extends SetType implements InterpretCollection {

    private final Set<InterpretValue> set = new HashSet<>();

    public InterpretSet(Type elementType) {
        super(elementType);
    }
    public InterpretSet(List<? extends InterpretValue> list) {
        super(list.get(0));
        set.addAll(list);
    }

    public Collection<InterpretValue> getValues() {
        return set;
    }

    @Override
    public InterpretValue getSingleValueAt(InterpretValue index) {
        return set.contains(index) ? InterpretBool.TRUE : InterpretBool.FALSE;
    }

    @Override
    public void setSingleValueAt(InterpretValue index, InterpretValue value) {
        if(value instanceof InterpretBool toAdd) {
            if(toAdd.getValue()) {
                add(index);
            } else {
                set.remove(index);
            }
        } else {
            throw programError("Cannot set set with " + value, ErrorBlock.Reason.OTHER);
        }
    }

    @Override
    public void add(InterpretValue value) {
        set.add(value);
    }

    @Override
    public void addAll(Collection<? extends InterpretValue> values) {
        this.set.addAll(values);
    }

    @Override
    public InterpretCollection emptyCopy() {
        return new InterpretSet(getElementType());
    }

    public InterpretSet clone() {
        InterpretSet ret = new InterpretSet(getElementType());
        ret.addAll(getValues());
        return ret;
    }

    @Override
    public String valueString() {
        return "{" + set.stream()
                .map(InterpretValue::valueString)
                .reduce((a, b) -> a + "," + b)
                .orElse("")
                + "}";
    }
}
