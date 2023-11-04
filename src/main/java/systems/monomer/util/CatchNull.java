package systems.monomer.util;

import org.jetbrains.annotations.Nullable;

public class CatchNull<T> {
    private final @Nullable T value;
    public CatchNull(@Nullable T value) {
        this.value = value;
    }

    public T resolve(T ifNull) {
        return value == null ? ifNull : value;
    }
}
