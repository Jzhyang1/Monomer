package systems.monomer.syntaxtree.controls;

import systems.monomer.interpreter.*;
import systems.monomer.types.AnyType;

public class RepeatNode extends ControlOperatorNode {
    public RepeatNode() {
        super("repeat");
    }
    public void matchTypes() {
        super.matchTypes();
        setType(new InterpretSequence(getType()));
    }

    public InterpretControlResult interpretControl(boolean previousSuccess, boolean previousFailure, InterpretValue previousValue) {
        //TODO unchecked asValue
        InterpretValue maybeRepetitions = getFirst().interpretValue().asValue();
        if(maybeRepetitions instanceof InterpretNumber<?> repetitions) {
            int numRepetitions = repetitions.getValue().intValue();
            InterpretSequence ret = new InterpretSequence(AnyType.ANY); //TODO find the actual type

            for(int i = 0; i < numRepetitions; i++) {
                InterpretResult result = getSecond().interpretValue();
                if(result.isValue())
                    ret.add(result.asValue());
                else if(result instanceof InterpretBreaking breaking){
                    if("continue".equals(breaking.getName())) continue;
                    else if("break".equals(breaking.getName()))
                        return new InterpretControlResult(false, result.asValue());
                    return new InterpretControlResult(false, result);
                }
            }
            return new InterpretControlResult(true, ret);
        } else {
            getFirst().throwError("Repeat operator requires a number for the number of repetitions");
            return null;
        }
    }
}
