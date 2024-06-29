package systems.monomer.syntaxtree.literals;

import systems.monomer.types.NumberType;

public class FloatNode extends LiteralNode {
    protected final Double value;

    public FloatNode(Double f) {
        super(f.toString());
        value = f;
    }

    @Override
    public void matchTypes() {
        setType(NumberType.FLOAT);
    }
}
