package systems.monomer.interpreter.operators;

import systems.monomer.execution.environmentDefaults.ConvertDefaults;
import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.variables.InterpretKey;
import systems.monomer.interpreter.variables.InterpretVariable;
import systems.monomer.interpreter.values.InterpretObject;
import systems.monomer.syntaxtree.operators.AssertTypeNode;
import systems.monomer.variables.InterpretOverloadable;

public class InterpretAssertTypeNode extends AssertTypeNode implements InterpretNode {
    public InterpretResult interpretValue() {
        InterpretNode second = getSecondInterpretNode();

        InterpretResult originalResult = second.interpretValue();
        if(!originalResult.isValue())
            return originalResult;

        InterpretValue originalValue = originalResult.asValue();

        InterpretKey convertFunc = (InterpretKey) getVariable(ConvertDefaults.NAME);
        InterpretOverloadable overloads = (InterpretOverloadable) convertFunc.getValue();

        return overloads.getOverload(convertBy).call(originalValue, InterpretObject.EMPTY);
    }

    @Override
    public InterpretVariable interpretVariable() {
        InterpretNode second = getSecondInterpretNode();
        return second.interpretVariable();
    }
}
