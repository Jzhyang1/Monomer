package systems.monomer.syntaxtree.controls;

import systems.monomer.interpreter.InterpretNumberValue;
import systems.monomer.interpreter.InterpretSequence;
import systems.monomer.interpreter.InterpretValue;

public class WhileNode extends ControlOperatorNode {
    public WhileNode() {
        super("while");
    }
    public void matchTypes() {
        super.matchTypes();
        setType(new InterpretSequence(getType()));
    }

    public InterpretControlResult interpretControl(boolean previousSuccess, boolean previousFailure, InterpretValue previousValue) {
        InterpretControlResult repetition;
        do {
            repetition = interpretControl(boolCondition -> {
            if (boolCondition.getValue()) {
                return new InterpretControlResult(true, getSecond().interpretValue());
            } else {
                return new InterpretControlResult(false, previousValue);
            }
        });
        } while (repetition.isSuccess);
        return repetition;  //TODO this is wrong
    }
}
