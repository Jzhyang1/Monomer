package systems.monomer.interpreter;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @param <T> The type of base value, can be Integer, Char, Float, Boolean
 */
public class InterpretNumberValue<T extends Number> extends InterpretValue {
    @Getter @Setter
    private T value;
    private String typeName;

    public InterpretNumberValue(T value) {
        this.value = value;
        typeName = value.getClass().getName();
    }

    public String valueString(){
        return value.toString();
    }
    public boolean typeContains(InterpretValue type) {
        return type instanceof InterpretNumberValue<?> baseType && baseType.typeName.equals(typeName);
    }

    public InterpretNumberValue<T> clone(){
        return (InterpretNumberValue<T>) super.clone();
    }
}
