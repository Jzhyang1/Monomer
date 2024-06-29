package systems.monomer.interpreter.literals;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.interpreter.values.InterpretString;
import systems.monomer.syntaxtree.literals.StringNode;

public class InterpretStringNode extends StringNode implements InterpretNode {

    public InterpretStringNode(String s) {
        super(s);
    }

    @Override
    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot interpret string value as variable");
    }

    @Override
    public InterpretValue interpretValue() {
        return new InterpretString(value);
    }
}
