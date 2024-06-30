package systems.monomer.types;

import lombok.Getter;

@Getter
public class Signature extends AnyType {
    public static final Signature ANYSIGNATURE = new Signature(AnyType.ANY, AnyType.ANY);
    private final Type returnType, argsType, namedArgsType;

    public Signature(Type args, Type returnType) {
        this.returnType = returnType;
        this.argsType = args;
        namedArgsType = ObjectType.EMPTY;
    }
    public Signature(Type args, Type namedArgs, Type returnType) {
        this.returnType = returnType;
        this.argsType = args;
        this.namedArgsType = namedArgs;
    }
    @Override
    public boolean equals(Object other) {
        return other instanceof Signature function &&
                getReturnType().equals(function.getReturnType()) &&
                getArgsType().equals(function.getArgsType()) &&
                getNamedArgsType().equals(function.getNamedArgsType());
    }

    @Override
    public boolean typeContains(Type other) {
        return other instanceof Signature sig &&
                getReturnType().typeContains(sig.getReturnType()) &&
                sig.getArgsType().typeContains(getArgsType());
//                && namedArgs.typeContains(sig.namedArgs);
    }

    @Override
    public int hashCode() {
        return returnType.hashCode() + argsType.hashCode() * 31 + this.getClass().hashCode() * 31 * 31;
    }

    public String toString() {
        return argsType + " -> " + returnType;
    }
}
