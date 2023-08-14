package systems.monomer.syntaxtree.controls;

import systems.monomer.interpreter.InterpretNumberValue;
import systems.monomer.interpreter.InterpretSequence;
import systems.monomer.interpreter.InterpretValue;

public class RepeatNode extends ControlOperatorNode {
    public RepeatNode() {
        super("repeat");
    }
    public void matchTypes() {
        super.matchTypes();
        setType(new InterpretSequence(getType()));
    }

    public InterpretControlResult interpretControl(boolean previousSuccess, boolean previousFailure, InterpretValue previousValue) {
        InterpretValue maybeRepetitions = getFirst().interpretValue();
        if(maybeRepetitions instanceof InterpretNumberValue<?> repetitions) {
            int numRepetitions = repetitions.getValue().intValue();
            for(int i = 0; i < numRepetitions; i++) {
                InterpretValue result = getSecond().interpretValue();
                //TODO handle break
            }
            return new InterpretControlResult(true, new InterpretSequence());
        } else {
            getFirst().throwError("Repeat operator requires a number for the number of repetitions");
            return null;
        }
    }
}
