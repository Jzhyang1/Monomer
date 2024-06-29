package systems.monomer.syntaxtree.literals;

import systems.monomer.types.CharType;

public class CharNode extends LiteralNode {
    protected Character value;
    public CharNode(Character c) {
        super("char");
        value = c;
    }

    @Override
    public void matchTypes() {
        setType(CharType.CHAR);
    }
}
