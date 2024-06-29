package systems.monomer.syntaxtree.controls;

import systems.monomer.interpreter.values.InterpretSequence;

public class WhileNode extends ControlOperatorNode {
    public WhileNode() {
        super("while");
    }
    public void matchTypes() {
        super.matchTypes();
        setType(new InterpretSequence(getType()));
    }
}
