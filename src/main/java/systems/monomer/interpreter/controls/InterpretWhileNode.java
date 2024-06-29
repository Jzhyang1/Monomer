package systems.monomer.interpreter.controls;

import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.interpreter.values.InterpretBreaking;
import systems.monomer.interpreter.values.InterpretSequence;
import systems.monomer.syntaxtree.controls.WhileNode;

import java.util.ArrayList;
import java.util.List;

import static systems.monomer.interpreter.controls.InterpretControls.InterpretControlResult;

public class InterpretWhileNode extends WhileNode implements InterpretControlNode {
    public InterpretControlResult interpretControl(boolean previousSuccess, boolean previousFailure, InterpretValue previousValue) {
        List<InterpretValue> repetitions = new ArrayList<>();
        boolean boolCondition = interpretCondition();
        while(boolCondition) {
            InterpretResult result = getSecondInterpretNode().interpretValue();
            if(result.isValue()) {
                repetitions.add(result.asValue());
            }
            else {
                InterpretControlResult controlResult = InterpretControls.resultOfBreak((InterpretBreaking) result, isThisExpression());
                if (controlResult != null) return controlResult;
            }
            boolCondition = interpretCondition();
        }
        return new InterpretControlResult(true, new InterpretSequence(repetitions));
    }

    @Override
    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot assign to result of while loop");
    }
}
