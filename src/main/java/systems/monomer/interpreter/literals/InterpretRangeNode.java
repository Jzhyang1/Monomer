package systems.monomer.interpreter.literals;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.interpreter.values.InterpretNumber;
import systems.monomer.interpreter.values.InterpretRange;
import systems.monomer.syntaxtree.literals.RangeNode;

public class InterpretRangeNode extends RangeNode implements InterpretNode {
    public InterpretRangeNode(boolean startInclusive, boolean stopInclusive) {
        super(startInclusive, stopInclusive);
    }

    @Override
    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot interpret range value as variable");
    }

    @Override
    public InterpretResult interpretValue() {
        InterpretResult start = getFirstInterpretNode().interpretValue();
        if (!start.isValue()) return start;
        InterpretResult stop = getSecondInterpretNode().interpretValue();
        if (!stop.isValue()) return stop;

        return new InterpretRange(
                start.asValue(),
                stop.asValue(),
                new InterpretNumber<Integer>(1),
                startInclusive,
                stopInclusive
        );
    }
}
