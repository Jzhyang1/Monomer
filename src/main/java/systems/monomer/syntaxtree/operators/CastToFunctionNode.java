package systems.monomer.syntaxtree.operators;

import systems.monomer.interpreter.InterpretResult;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.TypeContext;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.types.*;
import systems.monomer.variables.FunctionBody;
import systems.monomer.variables.OverloadedFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        Signature expectedSignature = (Signature) expectedType;

        if(actualType.typeContains(expectedSignature))
            return;
        else if(actualType == ANY)
            functionNode.setType(expectedSignature);

        if(actualType instanceof OverloadedFunctionType overloadedFunctionType) {
            functionIndex = overloadedFunctionType.randomAccessIndex(expectedSignature);
            if(functionIndex == -1)
                throw syntaxError("No function found with signature " + expectedSignature);

            Signature foundSignature = overloadedFunctionType.getSignature(functionIndex);
            if(!isIncompleteSignature(foundSignature))
                setType(foundSignature);
            else {
                FunctionBody functionBody = overloadedFunctionType.getFunction(functionIndex);
                Signature completedSignature = completedSignature(foundSignature, expectedSignature, functionBody);
                setType(completedSignature);
            }
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

    private void orderArgNames(Node node, Set<String> argNames, List<String> orderedArgNames){
        if(node.getUsage() == Usage.IDENTIFIER && argNames.contains(node.getName())) {
            orderedArgNames.add(node.getName());
        }
        else for (Node child : node.getChildren()) {
            orderArgNames(child, argNames, orderedArgNames);
        }
    }

    private boolean isIncompleteSignature(Signature signature) {
        return signature.getReturnType() == ANY || signature.getArgs() == ANY ||
                TupleType.typeInTuple(ANY, signature.getReturnType()) ||
                TupleType.typeInTuple(ANY, signature.getArgs()) ||
                ObjectType.typeInObject(ANY, signature.getNamedArgs());
    }
    private boolean canCompleteSignature(Signature signature) {
        return signature.getArgs() != ANY && !TupleType.typeInTuple(ANY, signature.getArgs());
    }

    private Signature completedSignature(Signature foundSignature, Signature expectedSignature, FunctionBody functionBody) {
        if(!isIncompleteSignature(foundSignature)) return foundSignature;
        if(!canCompleteSignature(expectedSignature)) throw syntaxError("Cannot complete signature " + foundSignature + " with " + expectedSignature);

        //mock types by filling foundSignature (the signature that the function is defined with)
        // with types from expectedSignature (the signature that the calling function used)
        // to find the resulting type of functionBody

        TypeContext context = new TypeContext(getParent());

        //todo move this logic to FunctionBody
        //add in the types from the expected signature to context
        //first the ordered arguments
        TupleType argTypes = TupleType.asTuple(expectedSignature.getArgs());
        TupleNode paramNodes = functionBody.getArgNodes();
        Set<String> paramNames = functionBody.getWrapper().getVariables().keySet();

        List<String> orderedParamNames = new ArrayList<>();
        orderArgNames(paramNodes, paramNames, orderedParamNames);

        for (int i = 0; i < orderedParamNames.size(); i++) {
            context.putVariableType(orderedParamNames.get(i), argTypes.getType(i));
        }

        //next the named arguments
        Type maybeNamedArgTypes = expectedSignature.getNamedArgs();
        if(maybeNamedArgTypes instanceof ObjectType namedArgTypes) {
            for (Map.Entry<String, Type> entry : namedArgTypes.getFields().entrySet()) {
                context.putVariableType(entry.getKey(), entry.getValue());
            }
        }

        //find the resulting type of functionBody
        Type resultingType = functionBody.getBody().testType(context);
        if(context.getReturnType() == null) {
            return new Signature(resultingType, foundSignature.getArgs(), foundSignature.getNamedArgs());
        } else {
            return new Signature(context.getReturnType(), foundSignature.getArgs(), foundSignature.getNamedArgs());
        }
    }
}
