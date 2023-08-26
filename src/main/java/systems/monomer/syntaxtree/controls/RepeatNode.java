package systems.monomer.syntaxtree.controls;

import systems.monomer.interpreter.InterpretNumber;
import systems.monomer.interpreter.InterpretSequence;
import systems.monomer.interpreter.InterpretValue;
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
        InterpretValue maybeRepetitions = getFirst().interpretValue();
        if(maybeRepetitions instanceof InterpretNumber<?> repetitions) {
            int numRepetitions = repetitions.getValue().intValue();
            InterpretSequence ret = new InterpretSequence(AnyType.ANY); //TODO find the actual type

            for(int i = 0; i < numRepetitions; i++) {
                InterpretValue result = getSecond().interpretValue();
                //TODO handle break
                ret.add(result);
            }
            return new InterpretControlResult(true, ret);
        } else {
            getFirst().throwError("Repeat operator requires a number for the number of repetitions");
            return null;
        }
    }
}
