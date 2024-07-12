package systems.monomer.util;

import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.function.BiFunction;

@UtilityClass
public class Util {
    public <T, U> T pairCheck(Collection<U> col1, Collection<U> col2, BiFunction<U, U, T> callback, T nullValue) {
        Iterator<U> iter1 = col1.iterator();
        Iterator<U> iter2 = col2.iterator();

        while (iter1.hasNext() && iter2.hasNext()) {
            T ret = callback.apply(iter1.next(), iter2.next());
            if (ret != nullValue) return ret;
        }

        return nullValue;
    }

    public <T extends Comparable<T>> int lowerBound(List<? extends T> col, T value) {
        int i = Collections.binarySearch(col, value);
        while(value.compareTo(col.get(i-1)) <= 0) {
            --i;
        }
        return i;
    }

    public Map EMPTY_MAP = Collections.unmodifiableMap(new HashMap<>());
}
