package systems.monomer.syntaxtree.literals;

import systems.monomer.types.primative.BoolType;

public class BoolNode extends LiteralNode {
    protected final boolean value;

    public BoolNode(boolean value){
        super("bool");
        this.value = value;
    }

    @Override
    public void matchTypes() {
        setType(BoolType.BOOL);
    }
}
