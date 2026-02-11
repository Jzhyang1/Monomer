package systems.monomer.syntaxtree.operators;

import systems.monomer.syntaxtree.Node;

public class WithNode extends OperatorNode {
    public WithNode() {
        super("with");
    }

    @Override
    public Node matchTypes() {
        super.matchTypes();
        setType(getFirst().getType());
        return this;
    }

    @Override
    public void setIsExpression(boolean isExpression) {
        super.setIsExpression(isExpression);
    }
}
