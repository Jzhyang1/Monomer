package systems.monomer.interpreter.literals;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.interpreter.values.InterpretNumber;
import systems.monomer.syntaxtree.literals.FloatNode;

public class InterpretFloatNode extends FloatNode implements InterpretNode {

    public InterpretFloatNode(Double f) {
        super(f);
    }

    @Override
    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot interpret float value as variable");
    }

    @Override
    public InterpretValue interpretValue() {
        return new InterpretNumber<>(value);
    }


}
