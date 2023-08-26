package systems.monomer.types;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Getter
public class TupleType extends AnyType {
    private final List<Type> types = new ArrayList<>();

    public void add(Type type) {
        types.add(type);
    }
    public void addAll(List<? extends Type> types) {
        this.types.addAll(types);
    }
    public void addAll(TupleType tuple) {
        types.addAll(tuple.types);
    }

    @Override
    public String valueString() {
        return types.stream()
                .map(Type::valueString)
                .reduce((a, b) -> a + "," + b)
                .orElse("");
    }

    @Override
    public TupleType clone() {
        TupleType cloned = (TupleType) super.clone();
        cloned.types.addAll(types);
        return cloned;
    }

    @Override
    public boolean typeContains(Type type) {
        return type instanceof TupleType tuple && tuple.types.size() == types.size() && IntStream.range(0, types.size()).allMatch(i -> types.get(i).typeContains(tuple.types.get(i)));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TupleType tuple && tuple.types.size() == types.size() && IntStream.range(0, types.size()).allMatch(i -> types.get(i).equals(tuple.types.get(i)));
    }
}
