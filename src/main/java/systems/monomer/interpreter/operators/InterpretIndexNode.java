package systems.monomer.interpreter.operators;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.variables.InterpretVariable;
import systems.monomer.syntaxtree.operators.IndexNode;

public class InterpretIndexNode extends IndexNode implements InterpretNode {
    @Override
    public InterpretValue interpretValue() {
        return ((InterpretVariable) getVariableKey()).getValue();
    }

    @Override
    public InterpretVariable interpretVariable() {
        return ((InterpretVariable) getVariableKey());
    }
}
