package systems.monomer.interpreter;

/**
 *
 * @param <T> The type of base value, can be Integer, Char, Float, Boolean
 */
public class InterpretNumberValue<T extends Number> extends InterpretValue {
    private T value;
    private String typeName;

    public InterpretNumberValue(T value) {
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
        return type instanceof InterpretNumberValue<?> baseType && baseType.typeName.equals(typeName);
    }

    public InterpretNumberValue<T> clone(){
        return (InterpretNumberValue<T>) super.clone();
    }
}
