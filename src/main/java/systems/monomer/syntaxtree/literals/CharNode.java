package systems.monomer.syntaxtree.literals;

import systems.monomer.syntaxtree.Node;
import systems.monomer.types.primitive.CharType;

public class CharNode extends LiteralNode {
    protected Character value;
    public CharNode(Character c) {
        super("char");
        value = c;
    }

    @Override
    public Node matchTypes() {
        setType(CharType.CHAR);
        return this;
    }
}
