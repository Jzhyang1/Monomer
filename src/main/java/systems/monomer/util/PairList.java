package systems.monomer.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.IntStream;

public class PairList<A,B> implements List<Pair<A,B>> {
    private ArrayList<Pair<A,B>> stack = new ArrayList<>();


    public Pair<A,B> get(int i) {
        return stack.get(i);
    }

    @Override
    public Pair<A, B> set(int index, Pair<A, B> element) {
        return stack.set(index, element);
    }

    @Override
    public void add(int index, Pair<A, B> element) {
        stack.add(index, element);
    }
    public void add(A a, B b) {
        stack.add(new Pair<>(a,b));
    }
    @Override
    public boolean add(Pair<A, B> abPair) {
        stack.add(abPair);
        return true;
    }
    @Override
    public boolean addAll(@NotNull Collection<? extends Pair<A, B>> c) {
        return stack.addAll(c);
    }
    @Override
    public boolean addAll(int index, @NotNull Collection<? extends Pair<A, B>> c) {
        return stack.addAll(index, c);
    }

    @Override
    public Pair<A, B> remove(int index) {
        throw new UnsupportedOperationException("Can not remove from PairList");
    }

    @Override
    public int indexOf(Object o) {
        if(o instanceof Pair<?,?> oPair) {
            return IntStream.range(0, stack.size())
                    .filter(i -> stack.get(i).getFirst().equals(oPair.getFirst()))
                    .findFirst()
                    .orElse(-1);
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if(o instanceof Pair<?,?> oPair) {
            return IntStream.iterate(stack.size() - 1, i -> i >= 0, i -> i - 1)
                    .filter(i -> stack.get(i).getFirst().equals(oPair.getFirst()))
                    .findFirst()
                    .orElse(-1);
        }
        return -1;
    }

    /**
     * backwards iterator
     */
    private class StackIterator implements ListIterator<Pair<A, B>> {
        private int index = stack.size();

        public StackIterator() {}
        public StackIterator(int index) {
            this.index = index;
        }

        @Override
        public boolean hasNext() {
            return index == 0;
        }
        @Override
        public Pair<A, B> next() {
            return stack.get(--index);
        }
        @Override
        public boolean hasPrevious() {
            return index == stack.size();
        }
        @Override
        public Pair<A, B> previous() {
            return stack.get(index++);
        }
        @Override
        public int nextIndex() {
            return index - 2;
        }
        @Override
        public int previousIndex() {
            return index;
        }
        @Override
        public void remove() {
            throw new UnsupportedOperationException("Can not remove from PairList");
        }
        @Override
        public void set(Pair<A, B> abPair) {
            throw new UnsupportedOperationException("Can not set in PairList");
        }
        @Override
        public void add(Pair<A, B> abPair) {
            throw new UnsupportedOperationException("Can not add to PairList");
        }
    }

    @NotNull
    @Override
    public ListIterator<Pair<A, B>> listIterator() {
        return new StackIterator();
    }

    @NotNull
    @Override
    public ListIterator<Pair<A, B>> listIterator(int index) {
        return new StackIterator(index);
    }

    @NotNull
    @Override
    public List<Pair<A, B>> subList(int fromIndex, int toIndex) {
        return stack.subList(fromIndex, toIndex);
    }

    @Override
    public int size() {
        return stack.size();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Can not search PairList");
    }

    @NotNull
    @Override
    public Iterator<Pair<A, B>> iterator() {
        return new StackIterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return stack.toArray();
    }
    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return stack.toArray(a);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException("Can not search PairList");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Can not remove from PairList");
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException("Can not remove from PairList");
    }
    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException("Can not remove from PairList");
    }

    @Override
    public void clear() {
        stack.clear();
    }

    @Override
    public String toString() {
        return stack.toString();
    }
}
