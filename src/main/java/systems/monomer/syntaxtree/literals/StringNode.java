package systems.monomer.syntaxtree.literals;

import systems.monomer.types.collection.StringType;

public class StringNode extends LiteralNode {
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
