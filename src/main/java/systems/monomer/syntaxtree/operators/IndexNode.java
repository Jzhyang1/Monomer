package systems.monomer.syntaxtree.operators;

import org.jetbrains.annotations.NotNull;
import systems.monomer.types.*;
import systems.monomer.types.collection.CollectionType;
import systems.monomer.variables.IndexKey;

public class IndexNode extends OperatorNode {
    private final IndexKey indexKey;

    public IndexNode() {
        super("index");
        indexKey = new IndexKey(this);
    }

    @Override
    public void matchTypes() {
        super.matchTypes();
        if(getFirst().getType() instanceof CollectionType colType) {
            Type indexType = getSecond().getType();
            Type accessedType = colType.indexResult(indexType);
            setType(accessedType);
        } else {
            throw syntaxError("Cannot index into non-collection type: " + getFirst().getType());
        }
    }

    @Override
    @NotNull
    public IndexKey getVariableKey() {
        return indexKey;
    }
}
