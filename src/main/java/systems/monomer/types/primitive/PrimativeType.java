package systems.monomer.types.primitive;

import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.Type;

public abstract class PrimativeType<T extends InterpretValue> implements Type {
    @Override
    public boolean typeContains(Type type) {
        int serialGap = type.serial() - this.serial();
        return 0 <= serialGap && serialGap < 0x00080000;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PrimativeType<?> prim && prim.serial() == serial();
    }

    @Override
    public int serial() {
        return 800_000;
    }

    @Override
    public abstract T defaultValue();
}
