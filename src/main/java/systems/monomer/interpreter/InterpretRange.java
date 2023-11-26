package systems.monomer.interpreter;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import systems.monomer.types.Type;

import java.util.Collection;
import java.util.Iterator;

@Getter
public class InterpretRange extends InterpretCollection implements InterpretValue, Comparable<InterpretRange> {
    private final InterpretValue start, stop, step;
    private final boolean startInclusive, stopInclusive;

    private static Type checkTypes(InterpretValue start, InterpretValue stop, InterpretValue step) {
        Type elementType = start.getType();
        if(!elementType.equals(stop.getType()))
            throw new Error(start + " and " + stop + " must have same type");
        if(!elementType.equals(step.getType()))
            throw new Error(start + " and " + step + " must have same type");
        return elementType;
    }

    public InterpretRange(InterpretValue start, InterpretValue stop, InterpretValue step) {
        super(checkTypes(start, stop, step));
        this.start = start;
        this.stop = stop;
        this.step = step;
        this.startInclusive = true;
        this.stopInclusive = true;
    }

    public InterpretRange(InterpretValue start, InterpretValue stop, InterpretValue step, boolean startInclusive, boolean stopInclusive) {
        super(checkTypes(start, stop, step));
        this.start = start;
        this.stop = stop;
        this.step = step;
        this.startInclusive = startInclusive;
        this.stopInclusive = stopInclusive;
    }

    public Collection<? extends InterpretValue> getValues() {
        throw new Error("TODO unimplemented");
    }

    @Override
    public void add(InterpretValue value) {
        throw new Error("Unable to add to range");
    }

    @Override
    public Iterator<? extends InterpretValue> iterator() {
        return new InterpretRangeIterator(start, stop, step, startInclusive, stopInclusive);
    }

    /**
     * Returns a union of two adjacent or overlapping ranges, and the ranges must have the same step.
     * @param other an overlapping range with the same step
     * @return a union of the two ranges
     */
    public InterpretRange union(InterpretRange other) {
        if(!getType().equals(other.getType()))
            throw new Error("cannot union ranges of different types");
        if(!step.equals(other.step))
            throw new Error("cannot union ranges with different steps");

        //TODO this only works for integers as of now
        int thisStart = ((InterpretNumber) start).getValue().intValue();
        int thisStop = ((InterpretNumber) stop).getValue().intValue();
        int otherStart = ((InterpretNumber) other.start).getValue().intValue();
        int otherStop = ((InterpretNumber) other.stop).getValue().intValue();

        if(thisStart > otherStart) {
            if (thisStop < otherStop)
                return other;
            else
                return new InterpretRange(other.start, stop, step);
        }
        else {
            if (thisStop < otherStop)
                return new InterpretRange(start, other.stop, step);
            else
                return this;
        }
    }

    @Override
    public int compareTo(@NotNull InterpretRange o) {
        //TODO this only works for integers as of now
        int thisStart = ((InterpretNumber) start).getValue().intValue();
        int otherStart = ((InterpretNumber) o.start).getValue().intValue();
        return startInclusive ? thisStart - otherStart : thisStart - otherStart - 1;
    }

    private static final class InterpretRangeIterator implements Iterator<InterpretValue> {
        private final InterpretValue stop, step;
        private final boolean stopInclusive;
        private InterpretValue next;

        public InterpretRangeIterator(InterpretValue start, InterpretValue stop, InterpretValue step, boolean startInclusive, boolean stopInclusive) {
            this.next = start;
            this.stop = stop;
            this.step = step;
            this.stopInclusive = stopInclusive;
            if(!startInclusive && hasNext()) next();
        }

        @Override
        public boolean hasNext() {
            //TODO this only works for integers as of now
            int thisStart = ((InterpretNumber) next).getValue().intValue();
            int thisStop = ((InterpretNumber) stop).getValue().intValue();

            return thisStart < thisStop || (stopInclusive && thisStart == thisStop);
        }

        @Override
        public InterpretValue next() {
            InterpretValue ret = next;

            //TODO this only works for integers as of now
            int thisStart = ((InterpretNumber) next).getValue().intValue();
            int thisStep = ((InterpretNumber) step).getValue().intValue();
            next = new InterpretNumber(thisStart + thisStep);

            return ret;
        }
    }
}
