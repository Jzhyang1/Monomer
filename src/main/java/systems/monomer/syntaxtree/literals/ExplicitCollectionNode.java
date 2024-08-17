package systems.monomer.syntaxtree.literals;

import systems.monomer.types.Type;
import systems.monomer.types.collection.CollectionType;
import systems.monomer.types.collection.SequenceType;
import systems.monomer.types.pseudo.AnyType;

public abstract class ExplicitCollectionNode extends LiteralNode {
    public ExplicitCollectionNode(String name) {
        super(name);
    }

    @Override
    public void matchTypes() {
        super.matchTypes();

        if(getChildren().isEmpty()) {
            setType(getCollectionType(AnyType.ANY));
            return;
        }
        Type t = get(0).getType();
        if(SequenceType.SEQUENCE.typeContains(t)) {
            setType(getCollectionType(((CollectionType)t).getElementType()));
            return;
        }

        for(int i = size() - 1; i >= 0; --i) {
            if(!t.typeContains(get(i).getType())) {
                throw syntaxError("Types of elements in collection do not match");
            }
        }
        setType(getCollectionType(t));
    }

    protected abstract CollectionType getCollectionType(Type elementType);
}
