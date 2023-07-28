package systems.monomer.interpreter;

import systems.monomer.syntaxTree.Node;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class InterpretSet extends InterpretCollectionValue{

    private final Set<InterpretValue> set;

    public InterpretSet(Collection<? extends Node> list) {
        set = new HashSet<>();
        getValues().addAll(list.stream().map(Node::interpretValue).toList());
    }

    public void add(Node node) {
        getValues().add(node.interpretValue());
    }

    protected Collection<InterpretValue> getValues() {
        throw new Error("TODO unimplemented");
    }

    public InterpretSet clone() {
        throw new Error("TODO unimplemented");
    }
}
