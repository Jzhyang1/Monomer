package systems.monomer.syntaxtree.literals;

import systems.monomer.types.primitive.FloatType;

public class FloatNode extends LiteralNode {
    protected final Double value;

    public FloatNode(Double f) {
        super(f.toString());
        value = f;
    }

    @Override
    public void matchTypes() {
        setType(FloatType.FLOAT);
    }
}
