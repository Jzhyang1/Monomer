package systems.monomer.syntaxtree.controls;

import systems.monomer.interpreter.InterpretValue;

public class AnyNode extends ControlOperatorNode {
    public AnyNode() {
        super("any");
    }

    public InterpretControlResult interpretControl(boolean previousSuccess, boolean previousFailure, InterpretValue previousValue) {
        return interpretControl(boolCondition -> {
            if (boolCondition || previousSuccess) {
                return new InterpretControlResult(true, getSecond().interpretValue());
            } else {
                return new InterpretControlResult(false, previousValue);
            }
        });
    }
}
