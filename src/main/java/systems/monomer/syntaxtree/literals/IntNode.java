package systems.monomer.syntaxtree.literals;

import systems.monomer.types.primitive.IntType;

public class IntNode extends LiteralNode {
    protected final Integer value;

    public IntNode(Integer i) {
        super(i.toString());
        value = i;
    }

    @Override
    public void matchTypes() {
        setType(IntType.INT);
    }
}
