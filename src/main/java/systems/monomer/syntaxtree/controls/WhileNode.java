package systems.monomer.syntaxtree.controls;

import systems.monomer.types.collection.SequenceType;

public class WhileNode extends ControlOperatorNode {
    public WhileNode() {
        super("while");
    }
    public void matchTypes() {
        super.matchTypes();
        setType(new SequenceType(getType()));
    }
}
