package systems.monomer.syntaxtree.controls;

import systems.monomer.interpreter.values.InterpretSequence;

public class RepeatNode extends ControlOperatorNode {
    public RepeatNode() {
        super("repeat");
    }
    public void matchTypes() {
        super.matchTypes();
        setType(new InterpretSequence(getType()));
    }

}
