package systems.monomer.syntaxtree.operators;

import static systems.monomer.types.pseudo.AnyType.ANY;

/**
 * The first child is the value
 * The second child is the type
 */
public class CastNode extends OperatorNode {
    public CastNode() {
        super("as");
    }

    public void matchTypes() {
        super.matchTypes();
        if(getType() == ANY) setType(getSecond().getType());
        if(getSecond().getType() == ANY) getSecond().setType(getType());
        if(getType() != getSecond().getType()) throw syntaxError("Internal error casting simultaneously to " + getFirst().getType() + " and " + getSecond().getType());
        if(!getFirst().getType().typeContains(getSecond().getType())) throw syntaxError("Cannot cast " + getFirst().getType() + " to " + getSecond().getType());
    }
}
