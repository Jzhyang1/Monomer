package systems.monomer.syntaxtree.controls;

import systems.monomer.interpreter.values.InterpretSequence;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.collection.SequenceType;

public class RepeatNode extends ControlOperatorNode {
    public RepeatNode() {
        super("repeat");
    }
    public Node matchTypes() {
        super.matchTypes();
        setType(new SequenceType(getType()));
        return this;
    }

}
