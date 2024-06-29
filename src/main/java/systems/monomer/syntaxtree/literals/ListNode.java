package systems.monomer.syntaxtree.literals;

import systems.monomer.syntaxtree.Node;
import systems.monomer.types.CollectionType;
import systems.monomer.types.ListType;
import systems.monomer.types.SequenceType;
import systems.monomer.types.Type;

import java.util.Collection;

public class ListNode extends LiteralNode {

    public ListNode() {
        super("list");
    }

    public ListNode(Collection<? extends Node> list) {
        super("list");
        addAll(list);
    }

    public void matchTypes() {
        super.matchTypes();
        if(getChildren().isEmpty()) {
            setType(ListType.LIST);
            return;
        }
        Type t = get(0).getType();
        if(SequenceType.isSequence(t)) {
            setType(new ListType(((CollectionType)t).getElementType()));
            return;
        }

        for(int i = size() - 1; i >= 0; --i) {
            if(!t.typeContains(get(i).getType())) {
                throw syntaxError("Types of elements in list do not match");
            }
        }
        setType(new ListType(t));
    }

    @Override
    public ListType getType() {
        return (ListType) super.getType();
    }
}
