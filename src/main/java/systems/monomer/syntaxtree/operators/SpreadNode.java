package systems.monomer.syntaxtree.operators;

import systems.monomer.syntaxtree.Node;
import systems.monomer.types.collection.CollectionType;
import systems.monomer.types.collection.SequenceType;
import systems.monomer.types.Type;

public class SpreadNode extends OperatorNode {
    public SpreadNode() {
        super("spread");
    }

    @Override
    public Node matchTypes() {
        super.matchTypes();
        Type operandType = getFirst().getType();
        if(operandType instanceof CollectionType operandCollectionType)
            setType(new SequenceType(operandCollectionType.getElementType()));
        else
            throw syntaxError("Cannot spread non-collection type " + operandType);
        return this;
    }
}
