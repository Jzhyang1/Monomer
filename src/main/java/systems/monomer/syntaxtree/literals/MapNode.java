package systems.monomer.syntaxtree.literals;

import systems.monomer.types.Type;
import systems.monomer.types.collection.CollectionType;
import systems.monomer.types.collection.MapType;
import systems.monomer.types.tuple.TupleType;

public class MapNode extends ExplicitCollectionNode {
    public MapNode() {
        super("map");
    }

    @Override
    protected CollectionType getCollectionType(Type elementType) {
        if(TupleType.EMPTY.typeContains(elementType)) {
            return new MapType((TupleType) elementType);
        } else {
            throw syntaxError("Map elements must be tuples");
        }
    }
}
