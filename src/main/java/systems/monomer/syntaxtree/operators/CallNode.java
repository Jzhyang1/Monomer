package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretFunction;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.types.AnyType;
import systems.monomer.types.Signature;
import systems.monomer.types.TupleType;
import systems.monomer.types.Type;
import systems.monomer.variables.VariableKey;

public class CallNode extends OperatorNode {
    private InterpretFunction function;

    public CallNode() {
        super("call");
    }

    public Type getSignature() {
        return new Signature(getType(), getSecond().getType());
    }

    public InterpretValue interpretValue() {
//        return getFirst().interpretValue().call(getSecond().interpretValue());
        return function.call(getSecond().interpretValue());
    }

    @Override
    public void matchTypes() {
        super.matchTypes();
        Type argType = TupleType.asTuple(getSecond().getType());    //TODO fix the initial setting of signatures such that single args are not tuples
//        if(argType == null) argType = AnyType.ANY;
        Type returnType = getType();
        if(returnType == null) returnType = AnyType.ANY;

        function = getFirst().getVariableKey().matchingOverload(new Signature(returnType, argType));
        if(function == null)
            throwError("No matching overload for " + getFirst().getName() + "(" + argType + "):" + returnType);
        Type actualReturnType = function.getReturnType();
        //TODO find out why it returns null
        //TODO make this not change values within the function
        //TODO make this work with recursion
        if(actualReturnType == null || actualReturnType.equals(AnyType.ANY)) actualReturnType = function.testReturnType(argType);
        setType(actualReturnType);
    }

    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
