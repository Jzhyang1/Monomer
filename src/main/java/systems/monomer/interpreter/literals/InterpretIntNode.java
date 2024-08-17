package systems.monomer.interpreter.literals;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.variables.InterpretVariable;
import systems.monomer.interpreter.values.InterpretInt;
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
        return new InterpretInt(value);
    }

}
