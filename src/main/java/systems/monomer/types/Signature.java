package systems.monomer.types;

import lombok.Getter;

import static systems.monomer.interpreter.InterpretObject.EMPTY;

@Getter
public class Signature extends AnyType {
    public static final Signature ANYSIGNATURE = new Signature(AnyType.ANY, AnyType.ANY);
    private final Type returnType, args, namedArgs;

    public Signature(Type returnType, Type args) {
        this.returnType = returnType;
        this.args = args;
        namedArgs = ObjectType.EMPTY;
    }
    public Signature(Type returnType, Type args, Type namedArgs) {
        this.returnType = returnType;
        this.args = args;
        this.namedArgs = namedArgs;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Signature sig &&
                returnType.equals(sig.returnType) &&
                args.equals(sig.args) &&
                namedArgs.equals(sig.namedArgs);
    }

    @Override
    public boolean typeContains(Type other) {
        return other instanceof Signature sig &&
                returnType.typeContains(sig.returnType) &&
                sig.args.typeContains(args);
//                && namedArgs.typeContains(sig.namedArgs);
    }

    @Override
    public int hashCode() {
        return returnType.hashCode() + args.hashCode() * 31 + this.getClass().hashCode() * 31 * 31;
    }

    public String toString() {
        return args + " -> " + returnType;
    }
}
