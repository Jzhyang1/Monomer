package systems.merl.monomer.syntaxTree;

public abstract class LiteralNode extends Node {

    public Usage usage;

    public LiteralNode(String name) {
        super(name);
        usage = Usage.LITERAL;
    }

    public Usage getUsage() {
        return usage;
    }

}
