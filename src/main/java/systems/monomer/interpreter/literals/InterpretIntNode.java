package systems.monomer.interpreter.literals;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.interpreter.values.InterpretNumber;
import systems.monomer.syntaxtree.literals.IntNode;

public class InterpretIntNode extends IntNode implements InterpretNode {
    public InterpretIntNode(Integer i) {
        super(i);
    }

    @Override
    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot interpret integer value as variable");
    }

    @Override
    public InterpretValue interpretValue() {
        return new InterpretNumber<>(value);
    }

}
