package systems.monomer.variables;

import systems.monomer.interpreter.InterpretFunction;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.Type;
import systems.monomer.types.signature.Signature;

public class FunctionOverload implements InterpretFunction {
    private InterpretOverloadable parent;
    private int randomAccessIndex;

    public FunctionOverload(InterpretOverloadable parent, int randomAccessIndex) {
        this.parent = parent;
        this.randomAccessIndex = randomAccessIndex;
    }

    @Override
    public InterpretValue call(InterpretValue args, InterpretValue namedArgs) {
        return parent.getOverload(randomAccessIndex).call(args, namedArgs);
    }

    @Override
    public Type getReturnType() {
        return parent.getOverload(randomAccessIndex).getReturnType();
    }

    @Override
    public Type getArgsType() {
        return parent.getOverload(randomAccessIndex).getArgsType();
    }

    @Override
    public Type getNamedArgsType() {
        return parent.getOverload(randomAccessIndex).getNamedArgsType();
    }

    @Override
    public Signature getType() {
        return parent.getOverload(randomAccessIndex);
    }
}
