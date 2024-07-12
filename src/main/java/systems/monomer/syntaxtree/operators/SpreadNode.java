package systems.monomer.syntaxtree.operators;

import systems.monomer.types.plural.CollectionType;
import systems.monomer.types.plural.SequenceType;
import systems.monomer.types.Type;

public class SpreadNode extends OperatorNode {
    public SpreadNode() {
        super("spread");
    }

    @Override
    public void matchTypes() {
        super.matchTypes();
        Type operandType = getFirst().getType();
        if(operandType instanceof CollectionType operandCollectionType)
            setType(new SequenceType(operandCollectionType.getElementType()));
        else
            throw syntaxError("Cannot spread non-collection type " + operandType);
    }
}
