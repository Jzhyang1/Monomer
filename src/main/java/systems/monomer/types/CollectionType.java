package systems.monomer.types;

import lombok.Getter;

@Getter
public class CollectionType extends ObjectType {
    public static CollectionType COLLECTION = new CollectionType(AnyType.ANY);
    private final Type elementType;

    public CollectionType(Type elementType) {
        this.elementType = elementType;
    }

    @Override
    public String valueString() {
        return "collection{" + elementType.valueString() + "}";
    }

    @Override
    public CollectionType clone() {
        return (CollectionType) super.clone();
    }

    @Override
    public boolean typeContains(Type other) {
        return super.typeContains(other) && other instanceof CollectionType collection && elementType.typeContains(collection.elementType);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && obj instanceof CollectionType collection && elementType.equals(collection.elementType);
    }

    @Override
    public int hashCode() {
        return elementType.hashCode()*31 + this.getClass().hashCode();
    }

//    @Override //TODO
//    public InterpretValue defaultValue() {
//        return InterpretTuple.EMPTY;
//    }
}
