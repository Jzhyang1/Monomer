package systems.monomer.interpreter.controls;

import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.syntaxtree.controls.AnyNode;
import static systems.monomer.interpreter.controls.InterpretControls.InterpretControlResult;

public class InterpretAnyNode extends AnyNode implements InterpretControlNode {
    public InterpretControlResult interpretControl(boolean previousSuccess, boolean previousFailure, InterpretValue previousValue) {
        if (previousSuccess || interpretCondition()) {
            return new InterpretControlResult(true, getSecondInterpretNode().interpretValue());
        } else {
            return new InterpretControlResult(false, previousValue);
        }
    }

    @Override
    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot assign to result of any block");
    }
}
