package systems.merl.monomer.interpreter;

/**
 *
 * @param <T> The type of base value, can be Integer, Char, Float, Boolean
 */
public class InterpretBaseValue<T> extends InterpretValue {
    private T value;
    private String typeName;

    public InterpretBaseValue(T value) {
        this.value = value;
        typeName = value.getClass().getName();
    }

    public void setValue(T value){
        this.value = value;
    }
    public T getValue(){
        return value;
    }
    public String valueString(){
        return value.toString();
    }
    public boolean typeContains(InterpretValue type) {
        return type instanceof InterpretBaseValue<?> baseType && baseType.typeName.equals(typeName);
    }

    public InterpretBaseValue<T> copy(){
        return new InterpretBaseValue<>(value);
    }
}
