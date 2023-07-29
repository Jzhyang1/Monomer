package systems.monomer.interpreter;

import systems.monomer.syntaxTree.Node;

import java.util.Collection;

public class InterpretList extends InterpretCollectionValue {

    public InterpretList(){

    }

    public InterpretList(Collection<? extends Node> list) {
        getValues().addAll(list.stream().map(Node::interpretValue).toList());
    }

    protected Collection<InterpretValue> getValues() {
        throw new Error("TODO unimplemented");
    }

    public InterpretList clone() {
        throw new Error("TODO unimplemented");
    }
}
