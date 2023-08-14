package systems.monomer.syntaxtree.controls;

import systems.monomer.interpreter.InterpretValue;

public class IfNode extends ControlOperatorNode {
    public IfNode() {
        super("if");
    }

    public InterpretControlResult interpretControl(boolean previousSuccess, boolean previousFailure, InterpretValue previousValue) {
        return interpretControl(boolCondition -> {
            if (boolCondition.getValue()) {
                return new InterpretControlResult(true, getSecond().interpretValue());
            } else {
                return new InterpretControlResult(false, previousValue);
            }
        });
    }
}
