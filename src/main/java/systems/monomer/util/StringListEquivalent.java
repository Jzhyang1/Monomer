package systems.monomer.util;


import org.jetbrains.annotations.NotNull;
import systems.monomer.errorhandling.ErrorBlock;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static systems.monomer.errorhandling.ErrorBlock.programError;

public class StringListEquivalent implements List<Character>, Comparable<StringListEquivalent> {
    private String value;
    public StringListEquivalent(String initial) {
        value = initial;
    }

    public int size() {
        return value.length();
    }
    public boolean isEmpty() {
        return value.isEmpty();
    }
    public Character get(int index) {
        return value.charAt(index);
    }
    @NotNull
    public Iterator<Character> iterator() {
        return new CrudeListIterator<>(this);
    }
    public boolean add(Character character) {
        value = value + character;
        return true;
    }
    public void add(int index, Character element) {
        value = value.substring(0, index) + element + value.substring(index);
    }
    public boolean addAll(@NotNull Collection<? extends Character> c) {
        if(c instanceof StringListEquivalent s) {
            this.value = this.value + s.value;
        } else {
            StringBuilder builder = new StringBuilder(this.value);
            c.forEach(letter -> builder.append(letter));
            this.value = builder.toString();
        }
        return true;
    }
    public boolean addAll(int index, @NotNull Collection<? extends Character> c) {
if(c instanceof StringListEquivalent s) {
            this.value = this.value.substring(0, index) + s.value + this.value.substring(index);
        } else {
            StringBuilder builder = new StringBuilder(this.value.substring(0, index));
            c.forEach(letter -> builder.append(letter));
            builder.append(this.value.substring(index));
            this.value = builder.toString();
        }
        return true;
    }
    public void clear() {
        value = "";
    }


    public Character set(int index, Character element) {
        value = value.substring(0, index) + element + value.substring(index + 1);
        return element;
    }
    public boolean contains(Object o) {
        return o instanceof CharSequence cs && value.contains(cs);
    }
    public boolean containsAll(@NotNull Collection<?> c) {
        return c.stream().allMatch(this::contains);
    }
    public boolean remove(Object o) {
        int i = value.indexOf(o.toString());
        if (i == -1) return false;

        value = value.substring(0, i) + value.substring(i + 1);
        return true;
    }
    public Character remove(int index) {
        throw programError("Cannot remove from StringListEquivalent", ErrorBlock.Reason.SYNTAX);
    }
    public boolean removeAll(@NotNull Collection<?> c) {
        throw programError("Cannot remove from StringListEquivalent", ErrorBlock.Reason.SYNTAX);
    }
    public boolean retainAll(@NotNull Collection<?> c) {
        throw programError("Cannot remove from StringListEquivalent", ErrorBlock.Reason.SYNTAX);
    }


    public int indexOf(Object o) {
        return o instanceof Character c ? value.indexOf(c) : -1;
    }
    public int lastIndexOf(Object o) {
        return o instanceof Character c ? value.lastIndexOf(c) : -1;
    }

    @NotNull
    public ListIterator<Character> listIterator() {
        return new CrudeListIterator<>(this);
    }
    @NotNull
    public ListIterator<Character> listIterator(int index) {
        return new CrudeListIterator<>(this, index);
    }

    @NotNull
    public List<Character> subList(int fromIndex, int toIndex) {
        return new StringListEquivalent(value.substring(fromIndex, toIndex));
    }

    @NotNull
    public Object[] toArray() {
        return value.chars().mapToObj(c -> (char) c).toArray();
    }
    @NotNull
    public <T> T[] toArray(@NotNull T[] a) {
        if(a.length < value.length()) {
            return (T[]) toArray();
        }

        for (int i = 0; i < value.length(); i++) {
            a[i] = (T) (Character) value.charAt(i);
        }
        return a;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int compareTo(StringListEquivalent value) {
        return this.value.compareTo(value.value);
    }
}