package systems.monomer.interpreter;

import org.jetbrains.annotations.NotNull;
import systems.monomer.types.Type;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class InterpretRanges extends InterpretCollection implements Iterable<InterpretValue> {
    private final Set<InterpretRange> ranges = new TreeSet<>();

    public InterpretRanges(Type elementType) {
        super(elementType);
    }

    public InterpretRanges(InterpretRange initialRange) {
        super(initialRange.getElementType());
        ranges.add(initialRange);
    }

    public InterpretRanges(InterpretValue start, InterpretValue stop, InterpretValue step) {
        this(new InterpretRange(start, stop, step));
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
            //TODO optimize storage via merging ranges with overlapping bounds
            ranges.add(interpretRange);
        }
        else
            throw new Error("cannot add " + range + " to ranges");
    }

    @Override
    public int size() {
        return ranges.stream().mapToInt(InterpretRange::size).sum();
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
}
