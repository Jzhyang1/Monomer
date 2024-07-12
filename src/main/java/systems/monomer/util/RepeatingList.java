package systems.monomer.util;


import java.util.*;

public class RepeatingList<T> implements List<T> {
    public static class RepeatingIterator<T> implements ListIterator<T> {
        private final T element;
        int index = 0;

        public RepeatingIterator(T element) {
            this.element = element;
        }

        public RepeatingIterator(T element, int index) {
            this.element = element;
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public T next() {
            ++index;
            return element;
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public T previous() {
            return element;
        }

        @Override
        public int nextIndex() {
            return index + 1;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public void remove() {
            throw new RuntimeException("Cannot remove from RepeatingList");
        }

        @Override
        public void set(T t) {
            throw new RuntimeException("Cannot modify RepeatingList");
        }

        @Override
        public void add(T t) {
            throw new RuntimeException("Cannot add to RepeatingList");
        }
    }

    private final T element;

    public RepeatingList(T element) {
        this.element = element;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return element.equals(o);
    }

    @Override
    public Iterator<T> iterator() {
        return new RepeatingIterator<>(element);
    }

    @Override
    public Object[] toArray() {
        return List.of(element).toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        Arrays.fill(a, element);
        return a;
    }

    @Override
    public boolean add(T t) {
        throw new RuntimeException("Cannot add to RepeatingList");
    }

    @Override
    public boolean remove(Object o) {
        throw new RuntimeException("Cannot remove from RepeatingList");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return c.stream().allMatch(element::equals);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new RuntimeException("Cannot add to RepeatingList");
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new RuntimeException("Cannot add to RepeatingList");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new RuntimeException("Cannot remove from RepeatingList");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new RuntimeException("Cannot remove from RepeatingList");
    }

    @Override
    public void clear() {
        throw new RuntimeException("Cannot clear RepeatingList");
    }

    @Override
    public T get(int index) {
        return element;
    }

    @Override
    public T set(int index, T element) {
        throw new RuntimeException("Cannot modify RepeatingList");
    }

    @Override
    public void add(int index, T element) {
        throw new RuntimeException("Cannot add to RepeatingList");
    }

    @Override
    public T remove(int index) {
        throw new RuntimeException("Cannot remove from RepeatingList");
    }

    @Override
    public int indexOf(Object o) {
        return element.equals(o) ? 0 : -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        return element.equals(o) ? 0 : -1;
    }

    @Override
    public ListIterator<T> listIterator() {
        return new RepeatingIterator<>(element);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new RepeatingIterator<>(element, index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        List<T> ret = new ArrayList<>();
        for (int i = fromIndex; i < toIndex; ++i) ret.add(element);
        return ret;
    }
}
