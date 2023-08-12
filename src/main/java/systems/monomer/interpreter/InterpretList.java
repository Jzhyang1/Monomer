package systems.monomer.interpreter;

import systems.monomer.syntaxtree.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InterpretList extends InterpretCollectionValue {
    public static InterpretList EMPTY = new InterpretList();
    private List<InterpretValue> values = new ArrayList<>();

    public InterpretList(){}

    public InterpretList(Collection<? extends Node> list) {
        values.addAll(list.stream().map(Node::interpretValue).toList());
    }

    public Collection<InterpretValue> getValues() {
        return values;
    }
    @Override
    public String valueString() {
        return "[" + super.valueString() + "]";
    }

    public InterpretList clone() {
        throw new Error("TODO unimplemented");
    }
}
