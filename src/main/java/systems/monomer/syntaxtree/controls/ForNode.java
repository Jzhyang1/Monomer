package systems.monomer.syntaxtree.controls;
import systems.monomer.interpreter.InterpretCollection;
import systems.monomer.interpreter.InterpretSequence;
import systems.monomer.interpreter.InterpretValue;

import java.util.Iterator;

public class ForNode extends ControlOperatorNode {
    public ForNode() {
        super("for");
    }

    public void matchTypes() {
        super.matchTypes();
        setType(new InterpretSequence(getType()));
    }

    public InterpretControlResult interpretControl(boolean previousSuccess, boolean previousFailure, InterpretValue previousValue) {
        InterpretValue maybeIterable = getFirst().interpretValue();
        if(maybeIterable instanceof InterpretCollection iterable) {
            Iterator<InterpretValue> iter = iterable.getValues().iterator();
            while(iter.hasNext()) {
                //TODO set iterator variable within the Monomer loop
                InterpretValue result = getSecond().interpretValue();
                //TODO handle break
                iter.next();
            }
            return new InterpretControlResult(true, new InterpretSequence());
        } else {
            getFirst().throwError("For operator requires a collection for the control of repetitions");
            return null;
        }
    }
}
