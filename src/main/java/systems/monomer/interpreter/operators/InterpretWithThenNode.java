package systems.monomer.interpreter.operators;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.interpreter.values.InterpretTuple;
import systems.monomer.syntaxtree.operators.WithThenNode;

public class InterpretWithThenNode extends WithThenNode implements InterpretNode {
    public InterpretWithThenNode(String name) {
        super(name);
    }

    @Override
    public InterpretResult interpretValue() {
        InterpretResult result1 = getFirstInterpretNode().interpretValue();
        if(!result1.isValue()) return result1;

        InterpretResult result2 = getSecondInterpretNode().interpretValue();
        if(!result2.isValue()) return result2;

        if(!isThisExpression()) return InterpretTuple.EMPTY;
        return checkedResult(valueIndex == 0 ? result1 : result2);
    }

    public InterpretVariable interpretVariable() {
        InterpretVariable ret = null;

        if(valueIndex == 0) {
            ret = getFirstInterpretNode().interpretVariable();
            getSecondInterpretNode().interpretValue();
        } else {
            getFirstInterpretNode().interpretValue();
            ret = getSecondInterpretNode().interpretVariable();
        }

        return ret;
    }
}
