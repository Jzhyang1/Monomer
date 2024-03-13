package systems.monomer.syntaxtree.controls;

import systems.monomer.interpreter.InterpretValue;

public class AllNode extends ControlOperatorNode {
    public AllNode() {
        super("all");
    }

    public InterpretControlResult interpretControl(boolean previousSuccess, boolean previousFailure, InterpretValue previousValue) {
        if (previousFailure) {
            return new InterpretControlResult(false, previousValue);
        }
        return interpretControl(boolCondition -> {
            if (boolCondition) {
                return new InterpretControlResult(true, getSecond().interpretValue());
            } else {
                return new InterpretControlResult(false, previousValue);
            }
        });
    }
}
