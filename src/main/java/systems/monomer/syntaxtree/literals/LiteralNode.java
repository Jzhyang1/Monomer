package systems.monomer.syntaxtree.literals;

import systems.monomer.syntaxtree.Node;

public abstract class LiteralNode extends Node {
    protected LiteralNode(String name) {
        super(name);
    }
    protected LiteralNode(){
        super("literal");
    }

    public Usage getUsage() {
        return Usage.LITERAL;
    }
}
