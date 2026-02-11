package systems.monomer.syntaxtree.operators;

import org.jetbrains.annotations.NotNull;
import systems.monomer.syntaxtree.Node;
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
    public Node matchTypes() {
        super.matchTypes();
        if (!(getFirst().getType() instanceof CollectionType colType)) {
            throw syntaxError("Cannot index into non-collection type: " + getFirst().getType());
        }

        Type indexType = getSecond().getType();
        Type accessedType = colType.indexResult(indexType);
        setType(accessedType);
        return this;
    }

    @Override
    @NotNull
    public IndexKey getVariableKey() {
        return indexKey;
    }
}
