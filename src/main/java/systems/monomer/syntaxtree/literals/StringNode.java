package systems.monomer.syntaxtree.literals;

import systems.monomer.syntaxtree.Node;
import systems.monomer.types.plural.StringType;

public class StringNode extends LiteralNode {
    public static final Node EMPTY = init.stringNode("");
    protected final String value;


    public StringNode(String s) {
        super("string");
        value = s;
    }

    @Override
    public void matchTypes() {
        setType(StringType.STRING);
    }

    @Override
    public String toString(int indention) {
        return super.toString(indention) + " \"" + value + "\"";
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}
