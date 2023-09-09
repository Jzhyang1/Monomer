package systems.monomer.syntaxtree.controls;

import systems.monomer.interpreter.InterpretBreaking;
import systems.monomer.interpreter.InterpretResult;
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
                InterpretResult result = getSecond().interpretValue();
                if(!result.isValue() && result instanceof InterpretBreaking breaking) {
                    if("continue".equals(breaking.getName())) return new InterpretControlResult(true, null);
                    else if("break".equals(breaking.getName()))
                        return new InterpretControlResult(false, result.asValue());
                    else return new InterpretControlResult(false, result);
                }
                return new InterpretControlResult(true, result);
            } else {
                return new InterpretControlResult(false, previousValue);
            }
        });
        } while (repetition.isSuccess);
        return repetition;  //TODO this is wrong
    }
}
