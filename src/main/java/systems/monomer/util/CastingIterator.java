package systems.monomer.util;

import java.util.ListIterator;

public class CastingIterator<T, U> implements ListIterator<T> {
    private final ListIterator<U> original;
    public CastingIterator(ListIterator<U> original) {
        this.original = original;
    }


    @Override
    public boolean hasNext() {
        return original.hasNext();
    }

    @Override
    public T next() {
        return (T) original.next();
    }

    @Override
    public boolean hasPrevious() {
        return original.hasPrevious();
    }

    @Override
    public T previous() {
        return (T) original.previous();
    }

    @Override
    public int nextIndex() {
        return original.nextIndex();
    }

    @Override
    public int previousIndex() {
        return original.previousIndex();
    }

    @Override
    public void remove() {
        original.remove();
    }

    @Override
    public void set(T t) {
        original.set((U) t);
    }

    @Override
    public void add(T t) {
        original.add((U) t);
    }
}
