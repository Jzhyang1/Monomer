package systems.monomer.syntaxtree.literals;

import systems.monomer.syntaxtree.Node;
import systems.monomer.types.collection.CollectionType;
import systems.monomer.types.collection.ListType;
import systems.monomer.types.Type;

import java.util.Collection;

public class ListNode extends ExplicitCollectionNode {

    public ListNode() {
        super("list");
    }

    public ListNode(Collection<? extends Node> list) {
        super("list");
        addAll(list);
    }

    @Override
    protected CollectionType getCollectionType(Type elementType) {
        return new ListType(elementType);
    }

    @Override
    public ListType getType() {
        return (ListType) super.getType();
    }
}
