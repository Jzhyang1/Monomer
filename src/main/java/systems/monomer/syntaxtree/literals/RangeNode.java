package systems.monomer.syntaxtree.literals;

import systems.monomer.interpreter.values.InterpretRanges;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.collection.RangeType;


public class RangeNode extends LiteralNode {
    protected final boolean startInclusive;
    protected final boolean stopInclusive;

    public RangeNode(boolean startInclusive, boolean stopInclusive) {
        this.startInclusive = startInclusive;
        this.stopInclusive = stopInclusive;
    }
    public RangeNode(Node start, Node stop, boolean startInclusive, boolean stopInclusive) {
        this(startInclusive, stopInclusive);
        super.add(start);
        super.add(stop);
    }

    @Override
    public void matchTypes() {
        super.matchTypes();
        //TODO first and second could be different types
        setType(new RangeType(get(0).getType()));
    }
}
