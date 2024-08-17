package systems.monomer.interpreter.operators;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.variables.InterpretIndexKey;
import systems.monomer.interpreter.variables.InterpretVariable;
import systems.monomer.interpreter.values.*;
import systems.monomer.syntaxtree.operators.IndexNode;
import systems.monomer.variables.IndexKey;

import java.util.ArrayList;
import java.util.List;

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
