package systems.monomer.interpreter.operators;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.variables.InterpretVariable;
import systems.monomer.interpreter.values.InterpretTuple;
import systems.monomer.syntaxtree.operators.ThenNode;

public class InterpretThenNode extends ThenNode implements InterpretNode {

    @Override
    public InterpretResult interpretValue() {
        InterpretResult result1 = getFirstInterpretNode().interpretValue();
        if(!result1.isValue()) return result1;

        InterpretResult result2 = getSecondInterpretNode().interpretValue();
        if(!result2.isValue()) return result2;

        if(!isThisExpression()) return InterpretTuple.EMPTY;
        return result2;
    }


    public InterpretVariable interpretVariable() {
        getFirstInterpretNode().interpretValue();
        return getSecondInterpretNode().interpretVariable();
    }
}
