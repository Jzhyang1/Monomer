package systems.monomer.types;

import systems.monomer.interpreter.InterpretTuple;

public class Signature extends AnyType {
    private Type returnType;
    private Type args;

    public Signature(Type returnType, Type args) {
        this.returnType = returnType;
        this.args = args;
    }

    public Type getReturnType() {
        return returnType;
    }

    public Type getArgs() {
        return args;
    }
}
