package systems.monomer.syntaxtree.literals;

import systems.monomer.types.Type;
import systems.monomer.types.collection.CollectionType;
import systems.monomer.types.collection.SetType;

public class SetNode extends ExplicitCollectionNode {
    public SetNode(){
        super("set");
    }

    @Override
    protected CollectionType getCollectionType(Type elementType) {
        return new SetType(elementType);
    }
}
