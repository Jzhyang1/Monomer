package systems.monomer.types;

import lombok.Getter;

@Getter
public class Signature extends AnyType {
    public static final Signature ANYSIGNATURE = new Signature(AnyType.ANY, AnyType.ANY);
    private final Type returnType;
    private final Type args;

    public Signature(Type returnType, Type args) {
        this.returnType = returnType;
        this.args = args;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Signature sig && returnType.equals(sig.returnType) && args.equals(sig.args);
    }

    @Override
    public int hashCode() {
        return returnType.hashCode() + args.hashCode() * 31 + this.getClass().hashCode() * 31 * 31;
    }

    public String toString() {
        return args.valueString() + " -> " + returnType.valueString();
    }
}
