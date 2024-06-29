package systems.monomer.interpreter.controls;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.interpreter.values.InterpretTuple;
import systems.monomer.syntaxtree.controls.ControlGroupNode;

import java.util.List;

import static systems.monomer.interpreter.controls.InterpretControls.InterpretControlResult;

public class InterpretControlGroupNode extends ControlGroupNode implements InterpretNode {
    @Override
    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot assign to result of control group");
    }

    public InterpretResult interpretValue() {
        boolean previousSuccess = false, previousFailure = false;

        List<InterpretControlNode> children = (List) getChildrenInterpretNodes();
        int size = children.size();

        InterpretControlResult result = children.get(0).interpretControl(previousSuccess, previousFailure, InterpretTuple.EMPTY);
        for (int i = 1; i < size; i++) {
            if(result.isBroken) break;

            previousSuccess = previousSuccess || result.isSuccess;
            previousFailure = previousFailure || !result.isSuccess;
            result = children.get(i).interpretControl(previousSuccess, previousFailure, result.value.asValue());
        }
        return result.value;
    }
}
