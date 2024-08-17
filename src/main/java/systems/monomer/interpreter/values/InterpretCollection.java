package systems.monomer.interpreter.values;

import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.Type;

import java.util.Collection;
import java.util.Iterator;

import static systems.monomer.errorhandling.ErrorBlock.programError;

public interface InterpretCollection extends InterpretValue {
    public abstract Type getElementType();

    public abstract Collection<? extends InterpretValue> getValues();

    public default InterpretValue getValueAt(InterpretValue index) {
        if(index instanceof InterpretRange range) {
            InterpretCollection ret = emptyCopy();
            for (InterpretValue singleIndex : range) {
                ret.add(getSingleValueAt(singleIndex));
            }
            return ret;
        } else {
            return getSingleValueAt(index);
        }
    }
    public default void setValueAt(InterpretValue index, InterpretValue value) {
        if(index instanceof InterpretRange range) {
            if (value instanceof InterpretCollection collection) {
                if (collection.size() != range.size())
                    throw programError("Cannot set range of size " + range.size() + " with " + collection.size() + " values", ErrorBlock.Reason.OTHER);

                //paired iterators
                Iterator<? extends InterpretValue> valueIterator = collection.iterator();
                Iterator<? extends InterpretValue> rangeIterator = range.iterator();
                while(valueIterator.hasNext() && rangeIterator.hasNext()) {
                    InterpretValue singleIndex = rangeIterator.next();
                    InterpretValue singleValue = valueIterator.next();
                    setSingleValueAt(singleIndex, singleValue);
                }
            }
            else
                throw programError("Cannot set range with " + value, ErrorBlock.Reason.OTHER);
        } else {
            setSingleValueAt(index, value);
        }
    }

    public InterpretValue getSingleValueAt(InterpretValue index);
    public void setSingleValueAt(InterpretValue index, InterpretValue value);


    default Iterator<? extends InterpretValue> iterator() {
        return getValues().iterator();
    }

    default int size() {
        return getValues().size();
    }

    public abstract void add(InterpretValue value);
    public abstract void addAll(Collection<? extends InterpretValue> values);

    public abstract InterpretCollection emptyCopy();

    default String valueString() {
        return getValues().stream()
                .map(InterpretValue::valueString)
                .reduce((a, b) -> a + "," + b)
                .orElse("");
    }

    public default int compareValueTo(InterpretValue maybeo) {
        if(!(maybeo instanceof InterpretCollection o)) return compareTo(maybeo);

        Iterator<? extends InterpretValue> thisIterator = iterator();
        Iterator<? extends InterpretValue> otherIterator = o.iterator();

        while(thisIterator.hasNext() && otherIterator.hasNext()) {
            InterpretValue thisValue = thisIterator.next();
            InterpretValue otherValue = otherIterator.next();

            int comparison = thisValue.compareValueTo(otherValue);
            if(comparison != 0) return comparison;
        }

        if(thisIterator.hasNext()) return 1;
        if(otherIterator.hasNext()) return -1;

        return 0;
    }
}
