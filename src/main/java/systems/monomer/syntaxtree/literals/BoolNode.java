package systems.monomer.syntaxtree.literals;

import systems.monomer.syntaxtree.Node;
import systems.monomer.types.primitive.BoolType;

public class BoolNode extends LiteralNode {
    protected final boolean value;

    public BoolNode(boolean value){
        super("bool");
        this.value = value;
    }

    @Override
    public Node matchTypes() {
        setType(BoolType.BOOL);
        return this;
    }
}
