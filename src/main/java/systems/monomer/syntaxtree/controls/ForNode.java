package systems.monomer.syntaxtree.controls;
import systems.monomer.interpreter.InterpretCollection;
import systems.monomer.interpreter.InterpretSequence;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.AnyType;

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
            InterpretSequence ret = new InterpretSequence(AnyType.ANY); //TODO find the actual type

            while(iter.hasNext()) {
                //TODO set iterator variable within the Monomer loop
                InterpretValue result = getSecond().interpretValue();
                //TODO handle break
                ret.add(result);
                iter.next();
            }
            return new InterpretControlResult(true, ret);
        } else {
            getFirst().throwError("For operator requires a collection for the control of repetitions");
            return null;
        }
    }
}
