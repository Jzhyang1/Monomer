package systems.monomer.interpreter.operators;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.syntaxtree.operators.CastToFunctionNode;
import systems.monomer.variables.OverloadedFunction;

public class InterpretCastToFunctionNode extends CastToFunctionNode implements InterpretNode {
    @Override
    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot assign to result of cast to function");
    }

    @Override
    public InterpretResult interpretValue() {
        InterpretNode first = getFirstInterpretNode();

        InterpretResult result = first.interpretValue();
        if(!result.isValue()) return result;

        InterpretValue value = result.asValue();

        if(functionIndex < 0) return value;
        if(value instanceof OverloadedFunction overloadedFunction)
            return overloadedFunction.getFunction(functionIndex);

        throw syntaxError("Expected a function, but got " + value);
    }
}
