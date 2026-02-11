package systems.monomer.syntaxtree.literals;

import systems.monomer.syntaxtree.Node;
import systems.monomer.types.collection.StringType;

import java.util.Collection;

public class StringBuilderNode extends LiteralNode {

    public StringBuilderNode(Collection<? extends Node> list) {
        super("stringbuilder");
        addAll(list);
    }

    @Override
    public Node matchTypes() {
        super.matchTypes();
        setType(StringType.STRING);
        return this;
    }
}
