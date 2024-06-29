package systems.monomer.interpreter.controls;

import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.controls.IfNode;

import systems.monomer.interpreter.InterpretVariable;

import static systems.monomer.interpreter.controls.InterpretControls.InterpretControlResult;

public class InterpretIfNode extends IfNode implements InterpretControlNode {
    @Override
    public InterpretControlResult interpretControl(boolean previousSuccess, boolean previousFailure, InterpretValue previousValue) {
        boolean boolCondition = interpretCondition();
        if (boolCondition) {
            return new InterpretControlResult(true, getSecondInterpretNode().interpretValue());
        } else {
            return new InterpretControlResult(false, previousValue);
        }
    }


    @Override
    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot assign to result of if statement");
    }
}
