package systems.monomer.interpreter.operators;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.interpreter.values.InterpretObject;
import systems.monomer.syntaxtree.operators.AssertTypeNode;
import systems.monomer.types.primative.ObjectType;
import systems.monomer.types.Type;
import systems.monomer.variables.FunctionBody;

public class InterpretAssertTypeNode extends AssertTypeNode implements InterpretNode {
    public InterpretResult interpretValue() {
        InterpretNode second = getSecondInterpretNode();

        InterpretResult originalResult = second.interpretValue();
        if(!originalResult.isValue())
            return originalResult;

        InterpretValue originalValue = originalResult.asValue();

        if(convertBy != null)
            return ((FunctionBody) convertBy).call(originalValue, InterpretObject.EMPTY);
        if(castBy != null) {
            Type to = getType();

            if(originalValue instanceof InterpretObject ofrom && to instanceof ObjectType oto)
                return castBy.apply(oto, ofrom);
            else
                throw runtimeError("Cannot cast object from " + originalValue + " to " + to);
        }

        if(originalValue.getType().equals(getType()))
            return originalValue;
        else
            throw runtimeError("Cannot convert value " + originalValue + " to " + getType());
    }

    @Override
    public InterpretVariable interpretVariable() {
        InterpretNode second = getSecondInterpretNode();
        return second.interpretVariable();
    }
}
