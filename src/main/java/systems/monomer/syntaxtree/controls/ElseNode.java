package systems.monomer.syntaxtree.controls;

import systems.monomer.interpreter.InterpretValue;

public class ElseNode extends ControlOperatorNode {
    public ElseNode() {
        super("else");
    }

    public InterpretControlResult interpretControl(boolean previousSuccess, boolean previousFailure, InterpretValue previousValue) {
        if(previousSuccess) {
            return new InterpretControlResult(true, previousValue);
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
