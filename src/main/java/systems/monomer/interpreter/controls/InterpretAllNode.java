package systems.monomer.interpreter.controls;

import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.syntaxtree.controls.AllNode;
import static systems.monomer.interpreter.controls.InterpretControls.InterpretControlResult;

public class InterpretAllNode extends AllNode implements InterpretControlNode {
    public InterpretControlResult interpretControl(boolean previousSuccess, boolean previousFailure, InterpretValue previousValue) {
        if (previousFailure) {
            return new InterpretControlResult(false, previousValue);
        }

        boolean boolCondition = interpretCondition();
        if (boolCondition) {
            return new InterpretControlResult(true, getSecondInterpretNode().interpretValue());
        } else {
            return new InterpretControlResult(false, previousValue);
        }
    }

    @Override
    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot assign to result of all block");
    }
}
