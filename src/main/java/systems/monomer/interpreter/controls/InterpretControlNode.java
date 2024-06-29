package systems.monomer.interpreter.controls;

import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.BoolType;

import static systems.monomer.errorhandling.ErrorBlock.programError;
import static systems.monomer.interpreter.controls.InterpretControls.InterpretControlResult;

public interface InterpretControlNode extends InterpretNode {
    InterpretControlResult interpretControl(boolean previousSuccess, boolean previousFailure, InterpretValue previousValue);

    /**
     * @return the interpreted boolean value of the condition
     * @throws ErrorBlock.ProgramErrorException
     */
    default Boolean interpretCondition() {
        InterpretResult conditionResult = getFirstInterpretNode().interpretValue();
        InterpretValue condition = conditionResult.asValue();
        if (!conditionResult.isValue() || !BoolType.BOOL.typeContains(condition)) {
            throw programError("Condition must be a boolean, got " + condition.getType());
        }
        return condition.<Boolean>getValue();
    }

    default InterpretResult interpretValue() {
        throw programError("Cannot interpret control independently (if you are getting this error, please report it as a bug)");
    }
}
