package systems.monomer.interpreter;

import systems.monomer.syntaxtree.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InterpretTuple extends InterpretCollectionValue {
    public static InterpretTuple EMPTY = new InterpretTuple(List.of());
    public static InterpretTuple toTuple(Collection<? extends Node> list) {
        return new InterpretTuple(list.stream().map(Node::interpretValue).toList());
    }

    private List<InterpretValue> tuple = new ArrayList<>();

    public InterpretTuple(Collection<? extends InterpretValue> list) {
        getValues().addAll(list);
    }

    public Collection<InterpretValue> getValues() {
        return tuple;
    }

    @Override
    public String valueString() {
        return "(" + super.valueString() + ")";
    }

    public InterpretTuple clone() {
        throw new Error("TODO unimplemented");
    }
}
