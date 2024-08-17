package systems.monomer.interpreter.values;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.Type;
import systems.monomer.types.collection.RangeType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static systems.monomer.errorhandling.ErrorBlock.programError;
import static systems.monomer.util.Util.boolAsInt;

@Getter
public final class InterpretRange extends RangeType implements InterpretCollection, Iterable<InterpretValue> {
    @Getter @Setter
    private InterpretValue start, stop, step;
    private final boolean startInclusive, stopInclusive;

    public InterpretRange(
            Type elementType,
            InterpretValue start, InterpretValue stop, InterpretValue step,
            boolean startInclusive, boolean stopInclusive
    ) {
        super(elementType);
        this.start = start;
        this.stop = stop;
        this.step = step;
        this.startInclusive = startInclusive;
        this.stopInclusive = stopInclusive;
    }

    public InterpretRange(Type elementType, boolean startInclusive, boolean stopInclusive) {
        super(elementType);
        this.startInclusive = startInclusive;
        this.stopInclusive = stopInclusive;
    }

    public Collection<? extends InterpretValue> getValues() {
        Collection<InterpretValue> ret = new ArrayList<>();
        for(InterpretValue value : this)
            ret.add(value);
        return ret;
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

    @Override
    public void add(InterpretValue value) {
        throw new Error("Unable to add to range");
    }

    @Override
    public void addAll(Collection<? extends InterpretValue> values) {
        throw programError("Ranges are not fully implemented", ErrorBlock.Reason.OTHER);
    }

    @Override
    public InterpretCollection emptyCopy() {
        throw programError("Ranges are not fully implemented", ErrorBlock.Reason.OTHER);
    }

    @Override
    public int size() {
        int startInt = this.start.<Number>getValue().intValue();
        int stopInt = this.stop.<Number>getValue().intValue();
        int stepInt = this.step.<Number>getValue().intValue();
        int adjust = (startInclusive ? 1 : 0) + (stopInclusive ? 1 : 0) - 1;
        return (stopInt - startInt) / stepInt + adjust;
    }

    @Override
    public Iterator<InterpretValue> iterator() {
        return new InterpretRangeIterator(start, stop, step, startInclusive, stopInclusive);
    }

    @Override
    public int compareValueTo(InterpretValue maybeo) {
        if(!(maybeo instanceof InterpretRange o)) return compareTo(maybeo);

        int comparison = start.compareTo(o.start);
        if(comparison != 0) {
            return comparison;
        } else {
            return boolAsInt(startInclusive) - boolAsInt(o.startInclusive);
        }
    }

    @Override
    public InterpretValue clone() {
        return new InterpretRange(getElementType(), start, stop, step, startInclusive, stopInclusive);
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
            int thisStart = next.getValue();
            int thisStop = stop.getValue();

            return thisStart < thisStop || (stopInclusive && thisStart == thisStop);
        }

        @Override
        public InterpretValue next() {
            InterpretValue ret = next;

            //TODO this only works for integers as of now
            int thisStart = next.getValue();
            int thisStep = step.getValue();
            next = new InterpretInt(thisStart + thisStep);

            return ret;
        }
    }
}
