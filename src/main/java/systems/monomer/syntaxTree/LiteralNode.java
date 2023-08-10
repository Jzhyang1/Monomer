package systems.monomer.syntaxTree;

public abstract class LiteralNode extends Node {
    public LiteralNode(String name) {
        super(name);
    }
    public LiteralNode(){
        super("literal");
    }

    public Usage getUsage() {
        return Usage.LITERAL;
    }

}