package systems.monomer.syntaxtree.literals;

import systems.monomer.types.primative.NumberType;

public class IntNode extends LiteralNode {
    protected final Integer value;

    public IntNode(Integer i) {
        super(i.toString());
        value = i;
    }

    @Override
    public void matchTypes() {
        setType(NumberType.INTEGER);
    }
}
