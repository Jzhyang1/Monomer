package systems.monomer.interpreter;

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

    public Number getValue() {
        return super.getValue();
    }

    @Override
    public InterpretNumber<T> clone() {
        return (InterpretNumber<T>) super.clone();
    }
}
