package systems.monomer.interpreter;

import systems.monomer.syntaxTree.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class InterpretTuple extends InterpretCollectionValue {

    List<InterpretValue> tuple;

    public InterpretTuple(Collection<? extends Node> list) {
        tuple = new ArrayList<>();
        getValues().addAll(list.stream().map(Node::interpretValue).toList());
    }

    protected Collection<InterpretValue> getValues() {
        throw new Error("TODO unimplemented");
    }

    public InterpretTuple clone() {
        throw new Error("TODO unimplemented");
    }
}