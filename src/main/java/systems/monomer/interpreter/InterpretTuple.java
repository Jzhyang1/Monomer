package systems.monomer.interpreter;

import systems.monomer.syntaxtree.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InterpretTuple extends InterpretCollectionValue {
    public static InterpretTuple EMPTY = new InterpretTuple(List.of());

    List<InterpretValue> tuple = new ArrayList<>();

    public InterpretTuple(Collection<? extends Node> list) {
        getValues().addAll(list.stream().map(Node::interpretValue).toList());
    }

    protected Collection<InterpretValue> getValues() {
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
