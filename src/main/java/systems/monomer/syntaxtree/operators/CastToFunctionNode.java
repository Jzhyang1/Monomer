package systems.monomer.syntaxtree.operators;

import systems.monomer.interpreter.InterpretResult;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.OverloadedFunctionType;
import systems.monomer.types.Signature;
import systems.monomer.types.Type;
import systems.monomer.variables.OverloadedFunction;

import static systems.monomer.types.AnyType.ANY;

/**
 * ConvertToFunctionNode is a ConvertNode optimized for converting to a function type
 * The children of ConvertToFunctionNode are:
 * <ol>
 *     <li> The value to be converted (some variant of a function) </li>
 * </ol>
 */
public class CastToFunctionNode extends CastNode {
    private int functionIndex = -1;

    @Override
    public void matchTypes() {
        Node functionNode = getFirst();
        functionNode.matchTypes();

        Type expectedType = getType();
        Type actualType = functionNode.getType();

        if(expectedType == ANY)
            setType(expectedType = actualType);
        if(!(expectedType instanceof Signature))
            throw syntaxError("The operations here should require a function type, but instead, the internal message required " + expectedType);

        Signature expectedFunctionType = (Signature) expectedType;

        if(actualType.typeContains(expectedFunctionType))
            return;
        else if(actualType == ANY)
            functionNode.setType(expectedFunctionType);

        if(actualType instanceof OverloadedFunctionType overloadedFunctionType) {
            functionIndex = overloadedFunctionType.randomAccessIndex(expectedFunctionType);
            if(functionIndex == -1)
                throw syntaxError("No function found with signature " + expectedFunctionType);

            setType(overloadedFunctionType.getOverload(expectedFunctionType));
        }
    }

    @Override
    public InterpretResult interpretValue() {
        InterpretResult result = getFirst().interpretValue();
        if(!result.isValue()) return result;

        if(functionIndex < 0) return result;
        if(result.asValue() instanceof OverloadedFunction overloadedFunction)
            return overloadedFunction.getFunction(functionIndex);

        throw syntaxError("Expected a function, but got " + result.asValue());
    }
}
