package systems.monomer.interpreter.values;

import org.jetbrains.annotations.NotNull;
import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.Type;
import systems.monomer.types.collection.RangeType;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import static systems.monomer.errorhandling.ErrorBlock.programError;

public class InterpretRanges extends RangeType implements InterpretCollection, Iterable<InterpretValue> {
    private final TreeSet<InterpretRange> ranges = new TreeSet<>();

    public InterpretRanges(Type elementType) {
        super(elementType);
    }

    public InterpretRanges(InterpretRange initialRange) {
        super(initialRange.getElementType());
        ranges.add(initialRange);
    }

    @Override
    public Collection<? extends InterpretValue> getValues() {
        return ranges;
    }

    @Override
    public @NotNull Iterator<InterpretValue> iterator() {
        return new InterpretRangesIterator(ranges);
    }

    @Override
    public void add(InterpretValue range) {
        if(range instanceof InterpretRange interpretRange) {
            //TODO do not assume that all ranges are continuous (i.e. merging ranges
            //  with overlapping bounds is not correct if their steps aren't equal)
            //optimize storage via merging ranges with overlapping bounds
            InterpretRange rightBefore = ranges.lower(interpretRange);
            InterpretRange rightAfter = ranges.higher(interpretRange);

            InterpretValue start = interpretRange.getStart(),
                    stop = interpretRange.getStop();
            boolean startInclusive = interpretRange.isStartInclusive(),
                    stopInclusive = interpretRange.isStopInclusive();

            if(rightBefore != null && rightBefore.getStop().compareValueTo(start) >= 0) {
                start = rightBefore.getStart();
                startInclusive = rightBefore.isStartInclusive();
                ranges.remove(rightBefore);
            }
            if(rightAfter != null && rightAfter.getStart().compareValueTo(stop) <= 0) {
                stop = rightAfter.getStop();
                stopInclusive = rightAfter.isStopInclusive();
                ranges.remove(rightAfter);
            }

            ranges.add(new InterpretRange(getElementType(), start, stop, interpretRange.getStep(), startInclusive, stopInclusive));
        }
        else
            throw new Error("cannot add " + range + " to ranges");
    }

    @Override
    public void addAll(Collection<? extends InterpretValue> values) {
        values.forEach(this::add);
    }

    @Override
    public InterpretCollection emptyCopy() {
        throw programError("Ranges are not fully implemented", ErrorBlock.Reason.OTHER);
    }

    @Override
    public int size() {
        return ranges.stream().mapToInt(InterpretRange::size).sum();
    }

    @Override
    public InterpretValue clone() {
        try {
            InterpretRanges cloned = (InterpretRanges) super.clone();
            cloned.ranges.clear();
            ranges.forEach(range -> cloned.ranges.add((InterpretRange) range.clone()));
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw programError("clone failed", ErrorBlock.Reason.OTHER);
        }
    }

    private static class InterpretRangesIterator implements Iterator<InterpretValue> {
        private final Iterator<InterpretRange> rangeIterator;
        private Iterator<? extends InterpretValue> valueIterator;

        public InterpretRangesIterator(Set<InterpretRange> ranges) {
            rangeIterator = ranges.iterator();
            valueIterator = rangeIterator.next().iterator();
        }

        @Override
        public boolean hasNext() {
            return valueIterator.hasNext() || rangeIterator.hasNext();
        }

        @Override
        public InterpretValue next() {
            if(!valueIterator.hasNext())
                valueIterator = rangeIterator.next().iterator();
            return valueIterator.next();
        }
    }

    @Override
    public int compareValueTo(InterpretValue other) {
        if (!(other instanceof InterpretRanges otherRanges)) {
            return compareTo(other);
        }

        //two pointers because ranges are sorted by start value
        Iterator<InterpretRange> thisIterator = ranges.iterator();
        Iterator<InterpretRange> otherIterator = otherRanges.ranges.iterator();

        InterpretRange thisRange = thisIterator.next();
        InterpretRange otherRange = otherIterator.next();

        while(thisIterator.hasNext() && otherIterator.hasNext()) {
            int comparison = thisRange.compareTo(otherRange);
            if(comparison >= 0) {
                otherRange = otherIterator.next();
            }
            if(comparison <= 0) {
                thisRange = thisIterator.next();
            }
        }

        if(thisIterator.hasNext() == otherIterator.hasNext()) {
            //they have both reached the last element
            //if the last element is equal, the ranges are equal
            return thisRange.compareTo(otherRange);
        } else {
            //if one has reached the last element and the other hasn't, the one that hasn't is greater
            return thisIterator.hasNext() ? 1 : -1;
        }
    }

    @Override
    public InterpretValue getSingleValueAt(InterpretValue index) {
        if(!(index instanceof InterpretInt i)) throw programError("Index is not an integer", ErrorBlock.Reason.RUNTIME);
        throw programError("Ranges are not fully implemented", ErrorBlock.Reason.OTHER); //TODO
    }

    @Override
    public void setSingleValueAt(InterpretValue index, InterpretValue value) {
        if (!(index instanceof InterpretInt i)) throw programError("Index is not an integer", ErrorBlock.Reason.RUNTIME);
        throw programError("Ranges are not fully implemented", ErrorBlock.Reason.OTHER); //TODO
    }
}
