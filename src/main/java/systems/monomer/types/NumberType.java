package systems.monomer.types;

import lombok.Getter;
import org.jetbrains.annotations.NonNls;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.values.InterpretNumber;
import systems.monomer.interpreter.InterpretValue;

@Getter
public class NumberType<T extends Number> extends AnyType {
    public static final NumberType<Integer> INTEGER = new NumberType<>(0);
    public static final NumberType<Float> FLOAT = new NumberType<>(0.0f);

    private final T value;
    private final @NonNls String typeName;

    protected NumberType(T value) {
        this.value = value;
        typeName = value.getClass().getName();
    }

    protected NumberType(T value, String typeName) {
        this.value = value;
        this.typeName = typeName;
    }

    public String valueString(){
        return typeName;
    }

    @Override
    public boolean typeContains(Type type) {
        return type instanceof NumberType<?>;
    }

    public NumberType<T> clone(){
        return (NumberType<T>) super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NumberType<?> num && num.typeName.equals(typeName);
    }

    @Override
    public InterpretValue defaultValue() {
        return new InterpretNumber<T>(value);
    }

    public CompileSize compileSize() {
        return new CompileSize(8);
    }
}
