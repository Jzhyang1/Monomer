package systems.monomer.syntaxtree.literals;

import systems.monomer.syntaxtree.Node;
import systems.monomer.types.StringType;

import java.util.Collection;

public class StringBuilderNode extends LiteralNode {

    public StringBuilderNode(Collection<? extends Node> list) {
        super("stringbuilder");
        addAll(list);
    }

    @Override
    public void matchTypes() {
        super.matchTypes();
        setType(StringType.STRING);
    }
}
