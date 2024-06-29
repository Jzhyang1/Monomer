package systems.monomer.interpreter.literals;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.interpreter.values.InterpretChar;
import systems.monomer.syntaxtree.literals.CharNode;

public class InterpretCharNode extends CharNode implements InterpretNode {
    public InterpretCharNode(Character c) {
        super(c);
    }

    @Override
    public InterpretChar interpretValue() {
        return new InterpretChar(value);
    }

    @Override
    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot interpret character value as variable");
    }
}
