package systems.monomer.types;

import systems.monomer.interpreter.InterpretTuple;

import java.util.Objects;

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

    public boolean equals(Object other) {
        if(other instanceof Signature sig) {
            return returnType.equals(sig.returnType) && args.equals(sig.args);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return returnType.hashCode() + args.hashCode() * 31 + this.getClass().hashCode() * 31 * 31;
    }

    public String toString() {
        return args.toString() + " -> " + returnType.toString();
    }
}
