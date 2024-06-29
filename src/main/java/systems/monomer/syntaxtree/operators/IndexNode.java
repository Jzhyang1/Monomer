package systems.monomer.syntaxtree.operators;

import systems.monomer.interpreter.values.*;
import systems.monomer.types.*;
import systems.monomer.variables.VariableKey;

public class IndexNode extends OperatorNode {
    public IndexNode() {
        super("index");
    }

    @Override
    public void matchTypes() {
        super.matchTypes();
        if (!ListType.LIST.typeContains(getFirst().getType()))    //TODO add support for other collection types
            throw syntaxError("Cannot index non-collection type " + getFirst().getType());
        if (!getSecond().getType().equals(NumberType.INTEGER) && !(getSecond().getType() instanceof InterpretRanges)) //TODO replace Instanceof check
            throw syntaxError("Cannot index with type " + getSecond().getType());

        Type elementType = ((CollectionType) getFirst().getType()).getElementType();
        setType(elementType);
    }

    @Override
    public VariableKey getVariableKey() {
        return new VariableKey() {
            @Override
            public void setType(Type type) {
                if (IndexNode.this.getType() == AnyType.ANY)
                    IndexNode.this.setType(type);
                else if (!IndexNode.this.getType().typeContains(type))
                    throw IndexNode.this.syntaxError("Type mismatch: " + IndexNode.this.getType() + " and " + type);
            }
        };
    }
}
