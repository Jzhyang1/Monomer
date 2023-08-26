package systems.monomer.types;

public class ListType extends CollectionType {
    public static final ListType LIST = new ListType(AnyType.ANY);

    public ListType(Type type) {
        super(type);
    }
}
