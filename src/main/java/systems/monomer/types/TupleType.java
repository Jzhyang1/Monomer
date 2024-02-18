package systems.monomer.types;

import lombok.Getter;
import systems.monomer.interpreter.InterpretTuple;
import systems.monomer.interpreter.InterpretValue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Getter
public class TupleType extends AnyType {
    public static final Type EMPTY = new TupleType(List.of());

    public static TupleType asTuple(Type type) {
        if(type instanceof TupleType tuple) return tuple;
        return new TupleType(List.of(type));
    }

    private final List<Type> types = new ArrayList<>();

    public TupleType() {}
    public TupleType(List<? extends Type> types) {
        this.types.addAll(types);
    }

    public void addType(Type type) {
        types.add(type);
    }
    public void addAll(List<? extends Type> types) {
        this.types.addAll(types);
    }
    public void addAll(TupleType tuple) {
        types.addAll(tuple.types);
    }

    public Type getType(int index) {
        return types.get(index);
    }

    public int size() {
        return types.size();
    }

    @Override
    public String valueString() {
        return types.stream()
                .map(Type::valueString)
                .reduce((a, b) -> a + "," + b)
                .orElse("");
    }

    @Override
    public InterpretValue defaultValue() {
        // InterpretTuple with default values
        return new InterpretTuple(types.stream()
                .map(Type::defaultValue)
                .toList());
    }

    @Override
    public TupleType clone() {
        TupleType cloned = (TupleType) super.clone();
        cloned.types.addAll(types);
        return cloned;
    }

    @Override
    public boolean typeContains(Type type) {
        return type instanceof TupleType tuple && tuple.types.size() >= types.size() && IntStream.range(0, types.size()).allMatch(i -> types.get(i).typeContains(tuple.types.get(i)));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TupleType tuple && tuple.types.size() == types.size() && IntStream.range(0, types.size()).allMatch(i -> types.get(i).equals(tuple.types.get(i)));
    }

    @Override
    public int hashCode() {
        return types.hashCode()*31 + this.getClass().hashCode();
    }

    @Override
    public String toString() {
        return this.getClass().toString() + types.toString();
    }
}
