package systems.monomer.util;

import java.util.List;
import java.util.ListIterator;

public class CrudeListIterator<T> implements ListIterator<T> {
    private final List<T> list;
    private int index = 0;
    public CrudeListIterator(List<T> list, int index) {
        this.index = index;
        this.list = list;
    }
    public CrudeListIterator(List<T> list) {
            this.list = list;
        }
    public boolean hasNext() {
        return index < list.size();
    }
    public T next() {
        return list.get(index++);
    }
    public boolean hasPrevious() {
        return index > 0;
    }
    public T previous() {
        return list.get(--index);
    }
    public int nextIndex() {
        return index + 1;
    }
    public int previousIndex() {
        return index - 1;
    }
    public void remove() {
        list.remove(index);
    }
    public void set(T character) {
        list.set(index, character);
    }
    public void add(T character) {
            list.add(index, character);
        }
}
