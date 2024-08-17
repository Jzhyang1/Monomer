package systems.monomer.interpreter.operators;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.variables.InterpretVariable;
import systems.monomer.interpreter.values.InterpretTuple;
import systems.monomer.syntaxtree.operators.WithNode;

public class InterpretWithNode extends WithNode implements InterpretNode {
    @Override
    public InterpretResult interpretValue() {
        InterpretResult result1 = getFirstInterpretNode().interpretValue();
        if(!result1.isValue()) return result1;

        InterpretResult result2 = getSecondInterpretNode().interpretValue();
        if(!result2.isValue()) return result2;

        if(!isThisExpression()) return InterpretTuple.EMPTY;
        return result1;
    }

    public InterpretVariable interpretVariable() {
        InterpretVariable ret = getFirstInterpretNode().interpretVariable();
        getSecondInterpretNode().interpretValue();

        return ret;
    }
}
