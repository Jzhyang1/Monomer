package systems.monomer.types;

public class SetType extends CollectionType {
    public static final SetType SET = new SetType(AnyType.ANY);

    public SetType(Type type) {
        super(type);
    }
}
