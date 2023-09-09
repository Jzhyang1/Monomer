package systems.monomer.syntaxtree.operators;

import org.jetbrains.annotations.Nullable;
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
    private @Nullable InterpretFunction function;

    public CallNode() {
        super("call");
    }

    public Type getSignature() {
        return new Signature(getType(), getSecond().getType());
    }

    public InterpretValue interpretValue() {
//        return getFirst().interpretValue().call(getSecond().interpretValue());
        return function.call(getSecond().interpretValue().asValue());
    }

    @Override
    public void matchTypes() {
        //recursion guard
        if(function != null) return;

        super.matchTypes();
        Type argType = TupleType.asTuple(getSecond().getType());    //TODO fix the initial setting of signatures such that single args are not tuples
//        if(argType == null) argType = AnyType.ANY;
        Type returnType = getType();
        if(returnType == null) returnType = AnyType.ANY;

        function = getFirst().getVariableKey().matchingOverload(new Signature(returnType, argType));
        Type actualReturnType = function.getReturnType();
        //TODO find out why it returns null
        //TODO make this not change values within the function
        if(actualReturnType.equals(AnyType.ANY))
            setType(function.testReturnType(argType));
        else if(returnType.equals(AnyType.ANY))
            setType(actualReturnType);
        else
            setType(returnType);
    }

    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
