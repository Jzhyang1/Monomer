package systems.monomer.types;

import lombok.Getter;
import systems.monomer.interpreter.InterpretMap;
import systems.monomer.interpreter.InterpretTuple;
import systems.monomer.interpreter.InterpretValue;

@Getter
public class MapType extends CollectionType {
    private final Type valueType;

    public MapType(Type elementType, Type valueType) {
        super(elementType);
        this.valueType = valueType;
    }

    @Override
    public String valueString() {
        return "map{" + getElementType().valueString() + ", " + valueType.valueString() + "}";
    }

    @Override
    public MapType clone(){
        return (MapType) super.clone();
    }

    @Override
    public boolean typeContains(Type type) {
        return type instanceof MapType mapType && super.typeContains(mapType) && valueType.typeContains(mapType.valueType);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MapType mapType && super.equals(mapType) && valueType.equals(mapType.valueType);
    }

//    @Override //TODO
//    public InterpretValue defaultValue() {
//        return InterpretMap.EMPTY;
//    }
}
