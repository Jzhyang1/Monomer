package systems.monomer.util;

import org.jetbrains.annotations.NotNull;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.Node;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CastingList<T, U> implements List<T> {
    private final List<U> original;
    public CastingList(List<U> original) {
        this.original = original;
    }


    //propagate list methods to original

    @Override
    public int size() {
        return original.size();
    }

    @Override
    public boolean isEmpty() {
        return original.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return original.contains(o);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new CastingIterator<>(original.listIterator());
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return original.toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return original.toArray(a);
    }

    @Override
    public boolean add(T interpretValue) {
        return original.add((U) interpretValue);
    }

    @Override
    public boolean remove(Object o) {
        return original.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return original.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        c.forEach(this::add);
        return true;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        for(int i = 0; i < c.size(); i++) {
            //TODO this is inefficient
            original.add(index + i, (U) c.toArray()[i]);
        }
        return true;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return original.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return original.retainAll(c);
    }

    @Override
    public void clear() {
        original.clear();
    }

    @Override
    public T get(int index) {
        return (T) original.get(index);
    }

    @Override
    public T set(int index, T element) {
        return (T) original.set(index, (U) element);
    }

    @Override
    public void add(int index, T element) {
        original.add(index, (U) element);
    }

    @Override
    public T remove(int index) {
        return (T) original.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return original.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return original.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator() {
        return new CastingIterator<>(original.listIterator());
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator(int index) {
        return new CastingIterator<>(original.listIterator(index));
    }

    @NotNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return new CastingList<>(original.subList(fromIndex, toIndex));
    }
}
