package systems.monomer.types;

public class IncompleteSignature extends Signature {

    public IncompleteSignature(Type args, Type returnType) {
        super(args, ANY);
    }

    public IncompleteSignature(Type args, Type namedArgs, Type returnType) {
        super(args, namedArgs, ANY);
    }
}
