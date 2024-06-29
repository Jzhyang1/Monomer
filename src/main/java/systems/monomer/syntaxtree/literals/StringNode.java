package systems.monomer.syntaxtree.literals;

import systems.monomer.syntaxtree.Node;
import systems.monomer.types.StringType;

import static systems.monomer.syntaxtree.Configuration.create;

public class StringNode extends LiteralNode {
    public static final Node EMPTY = create().stringNode("");
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
