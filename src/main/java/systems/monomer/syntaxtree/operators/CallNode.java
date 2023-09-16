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

public class CallNode extends OperatorNode {
    private @Nullable InterpretFunction function;

    public CallNode() {
        super("call");
    }

    public Type getSignature() {
        return new Signature(getType(), getSecond().getType());
    }

    public InterpretValue interpretValue() {
        //TODO function is cached from type, but it could have changed by assignment
        // And are functions defined in OverloadedFunction type instead of a value?
        if(function == null)
            return getFirst().interpretValue().asValue()
                    .call(getSecond().interpretValue().asValue());
        else
            return getFirst().interpretValue().asValue().call(getSecond().interpretValue().asValue());
    }

    @Override
    public void matchTypes() {
        //recursion guard
        if(function != null) return;

        super.matchTypes();
        Type argType = TupleType.asTuple(getSecond().getType());    //TODO fix the initial setting of signatures such that single args are not tuples
//        if(argType == null) argType = AnyType.ANY;
        Type returnType = getType();
        if(returnType == null) returnType = ANY;

        Type funcType = getFirst().getType();
        if(funcType instanceof OverloadedFunction overload) {
            function = overload.matchingOverload(new Signature(returnType, argType));
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
