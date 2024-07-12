package systems.monomer.syntaxtree.operators;

import systems.monomer.syntaxtree.Node;
import systems.monomer.types.*;
import systems.monomer.types.pseudo.IncompleteSignature;

import static systems.monomer.types.pseudo.AnyType.ANY;

/**
 * ConvertToFunctionNode is a ConvertNode optimized for converting to a function type
 * The children of ConvertToFunctionNode are:
 * <ol>
 *     <li> The value to be converted (some variant of a function) </li>
 * </ol>
 */
public class CastToFunctionNode extends CastNode {
    protected int functionIndex = -1;

    @Override
    public void matchTypes() {
        Node functionNode = getFirst();
        functionNode.matchTypes();

        Type expectedType = getType();  //should be a signature
        Type actualType = functionNode.getType();   //should be a signature or an overloadedFunction

        //if expectedType is not given, take on the actual type
        //if actual type is not given, take on the given type TODO an overloadedFunction and add a placeholder/forward-reference signature

        if(expectedType == ANY)
            setType(expectedType = actualType);
        if(!(expectedType instanceof Signature))
            throw syntaxError("A function type should be required here, but instead, the syntax tree required " + expectedType);
        if(actualType == ANY)
            functionNode.setType(actualType = expectedType);

        //if actualType is a signature, check that the two signatures match
        //if actualType is an overloadedFunction, check that it is in the function signature list

        Signature expectedSignature = (Signature) expectedType;

        if(actualType.typeContains(expectedType))
            return;
        else if(actualType instanceof OverloadedFunctionType overloadedFunctionType) {
            functionIndex = overloadedFunctionType.randomAccessIndex(expectedSignature);
            if(functionIndex == -1)
                throw syntaxError("No function found with signature " + expectedSignature);

            Signature foundSignature = overloadedFunctionType.getSignature(functionIndex);
            if(foundSignature.isComplete())
                setType(foundSignature);
            else {
                IncompleteSignature incompleteSignature = (IncompleteSignature) foundSignature;
                setType(incompleteSignature.completedSignature(expectedSignature));
            }
        }
        else {
            throw syntaxError("The signature " + actualType + " does not match the expected signature " + expectedType);
        }



//        if(actualType.typeContains(expectedType))
//            return;
//        else if(actualType instanceof Signature actualSignature) {
//            if(actualSignature.isComplete()) {
//                throw syntaxError("The signature " + actualType + " does not match the expected signature " + expectedType);
//            } else {
//                throw syntaxError("Internal error in handling the conversion from " + expectedType + " to " + actualType);
//            }
//        }
//
//
//        Signature expectedSignature = (Signature) expectedType;
//
//        if(actualType instanceof OverloadedFunctionType overloadedFunctionType) {
//            functionIndex = overloadedFunctionType.randomAccessIndex(expectedSignature);
//            if(functionIndex == -1)
//                throw syntaxError("No function found with signature " + expectedSignature);
//
//            Signature foundSignature = overloadedFunctionType.getSignature(functionIndex);
//            if(!isIncompleteSignature(foundSignature))
//                setType(foundSignature);
//            else {
//                Signature completedSignature = completedSignature(foundSignature, expectedSignature);
//                setType(completedSignature);
//            }
//        }
    }

//    private boolean isIncompleteSignature(Signature signature) {
//        return signature.getReturnType() == ANY || signature.getArgsType() == ANY ||
//                TupleType.typeInTuple(ANY, signature.getReturnType()) ||
//                TupleType.typeInTuple(ANY, signature.getArgsType()) ||
//                ObjectType.typeInObject(ANY, signature.getNamedArgsType());
//    }
}
