package systems.monomer.interpreter;

import lombok.Getter;
import lombok.Setter;
import systems.monomer.types.NumberType;

/**
 *
 * @param <T> The type of base value, can be Integer, Char, Float, Boolean
 */
public class InterpretNumber<T extends Number> extends NumberType implements InterpretValue {
    public InterpretNumber(T value) {
        super(value);
    }

    public String valueString(){
        return getValue().toString();
    }
}
