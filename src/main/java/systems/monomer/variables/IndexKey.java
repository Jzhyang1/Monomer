package systems.monomer.variables;

import lombok.Getter;
import systems.monomer.syntaxtree.operators.IndexNode;
import systems.monomer.types.Type;

@Getter
public class IndexKey extends Key {
    private final IndexNode owner;

    public IndexKey(IndexNode owner) {
        this.owner = owner;
    }

    @Override
    public Type getType() {
        return owner.getType();
    }

    @Override
    public void setType(Type type) {
        if(owner.getType().typeContains(type)) {
            owner.setType(type);
        } else {
            throw owner.syntaxError("Assigning type " + type + " to " + owner.getType());
        }
    }
}
