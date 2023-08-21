package systems.monomer.types;

import systems.monomer.interpreter.InterpretTuple;

public class Signature extends Type {
    private Type returnType;
    private InterpretTuple args;

    public Signature(Type returnType, InterpretTuple args) {
        this.returnType = returnType;
        this.args = args;
    }

    public Type getReturnType() {
        return returnType;
    }

    public InterpretTuple getArgs() {
        return args;
    }
}
