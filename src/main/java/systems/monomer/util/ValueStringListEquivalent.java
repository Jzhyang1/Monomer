package systems.monomer.util;

import org.jetbrains.annotations.NotNull;
import systems.monomer.interpreter.values.InterpretChar;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ValueStringListEquivalent implements List<InterpretChar>, Comparable<List<InterpretChar>> {
    private final List<Character> list;
    public ValueStringListEquivalent(List<Character> list) {
        this.list = list;
    }
    public ValueStringListEquivalent(String value) {
        this.list = new StringListEquivalent(value);
    }

    public int size() {
        return list.size();
    }
    public boolean isEmpty() {
        return list.isEmpty();
    }
    public boolean contains(Object o) {
        return list.contains(o);
    }
    @NotNull
    public Iterator<InterpretChar> iterator() {
        return new CrudeListIterator<>(this);
    }
    @NotNull
    public Object[] toArray() {
        return list.toArray();
    }
    @NotNull
    public <T> T[] toArray(@NotNull T[] a) {
        return list.toArray(a);
    }
    public boolean add(InterpretChar interpretChar) {
        return list.add(interpretChar.getValue());
    }
    public boolean remove(Object o) {
        return list.remove(o);
    }
    public boolean containsAll(@NotNull Collection<?> c) {
        return list.containsAll(c);
    }
    public boolean addAll(@NotNull Collection<? extends InterpretChar> c) {
        for (InterpretChar interpretChar : c) {
            list.add(interpretChar.getValue());
        }
        return true;
    }
    public boolean addAll(int index, @NotNull Collection<? extends InterpretChar> c) {
        int i = index;
        for (InterpretChar interpretChar : c) {
            list.add(i++, interpretChar.getValue());
        }
        return true;
    }
    public boolean removeAll(@NotNull Collection<?> c) {
        return list.removeAll(c);
    }
    public boolean retainAll(@NotNull Collection<?> c) {
        return list.retainAll(c);
    }
    public void clear() {
        list.clear();
    }
    public InterpretChar get(int index) {
        return new InterpretChar(list.get(index));
    }
    public InterpretChar set(int index, InterpretChar element) {
        return new InterpretChar(list.set(index, element.getValue()));
    }
    public void add(int index, InterpretChar element) {
        list.add(index, element.getValue());
    }
    public InterpretChar remove(int index) {
        return new InterpretChar(list.remove(index));
    }
    public int indexOf(Object o) {
        return list.indexOf(o);
    }
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }
    @NotNull
    public ListIterator<InterpretChar> listIterator() {
        return new CrudeListIterator<>(this);
    }
    @NotNull
    public ListIterator<InterpretChar> listIterator(int index) {
        return new CrudeListIterator<>(this, index);
    }
    @NotNull
    public List<InterpretChar> subList(int fromIndex, int toIndex) {
        return new ValueStringListEquivalent(list.subList(fromIndex, toIndex));
    }

    @Override
    public String toString() {
        return list.toString();
    }

    @Override
    public int compareTo(@NotNull List<InterpretChar> o) {
        //TODO possibly make ValueStringListEquivalent into ValueEquivalent<T>
        return list.toString().compareTo(o.toString());
    }
}