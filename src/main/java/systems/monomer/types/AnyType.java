package systems.monomer.types;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AnyType implements Type {
    public static final AnyType ANY = new AnyType();
    private boolean mutable = false;

    @Override
    public Type clone() {
        try {
            return (Type) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String valueString() {
        return "any";
    }

    @Override
    public boolean typeContains(Type type) {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Type;
    }
}
