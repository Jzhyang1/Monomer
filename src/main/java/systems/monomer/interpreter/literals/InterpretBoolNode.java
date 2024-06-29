package systems.monomer.interpreter.literals;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.interpreter.values.InterpretBool;
import systems.monomer.syntaxtree.literals.BoolNode;

public class InterpretBoolNode extends BoolNode implements InterpretNode {
    public InterpretBoolNode(boolean value) {
        super(value);
    }

    @Override
    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot interpret boolean value as variable");
    }

    public InterpretValue interpretValue() {
        return new InterpretBool(value);
    }
}
