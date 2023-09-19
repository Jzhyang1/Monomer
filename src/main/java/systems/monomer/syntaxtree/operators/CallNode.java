package systems.monomer.syntaxtree.operators;

import org.jetbrains.annotations.Nullable;
import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretFunction;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.*;
import systems.monomer.variables.Key;
import systems.monomer.variables.VariableKey;

import static systems.monomer.types.AnyType.ANY;

/**
 * A node representing a function call.
 *     <ul>
 *         <li>First child: the function to call</li>
 *         <li>Second child: the argument to pass to the function</li>
 *         <li>Third child: optional, the Structure representing named arguments</li>
 *     </ul>
 *     <br/>
 *     The type of the node is the return type of the function.
 *     the <b>getSignature</b> method returns the signature of the function call.
 */
public class CallNode extends OperatorNode {
    private int functionIndex = -1;

    public CallNode() {
        super("call");
    }

    public Signature getSignature() {
        return new Signature(getType(), TupleType.asTuple(getSecond().getType()));
    }

    public InterpretValue interpretValue() {
        //TODO why are functions defined in OverloadedFunction type instead of a value?
        if(functionIndex == -1)
            return getFirst().interpretValue().asValue()
                    .call(getSecond().interpretValue().asValue());
        else {
            //if(cache still matches) return function.call(getSecond().interpretValue().asValue()); //TODO
            //else
            return ((OverloadedFunction) getFirst().getType())
                    .getFunction(functionIndex)
                    .call(getSecond().interpretValue().asValue());
        }
    }

    @Override
    public void matchTypes() {
        //recursion guard
        if(functionIndex != -1) return;

        super.matchTypes();
        Type argType = getSecond().getType();    //TODO fix the initial setting of signatures such that single args are not tuples
//        if(argType == null) argType = AnyType.ANY;
        Type returnType = getType();
        if(returnType == null) returnType = ANY;

        Type funcType = getFirst().getType();
        if(funcType instanceof OverloadedFunction overload) {
            functionIndex = overload.randomAccessIndex(new Signature(returnType, argType));

            if(functionIndex == -1)
               throwError("No matching function found for " + argType + " -> " + returnType);

            InterpretFunction function = overload.getFunction(functionIndex);

            Type actualReturnType = function.getReturnType();
            //TODO find out why it returns null
            //TODO make this not change values within the function
            if(actualReturnType == ANY)
                setType(function.testReturnType(argType));
            else if(returnType == ANY)
                setType(actualReturnType);
            else
                setType(returnType);
        } else if (funcType instanceof Signature signature) {
            setType(signature.getReturnType());
        } else {
            getFirst().throwError("Expected function, got " + funcType);
        }
    }

    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
