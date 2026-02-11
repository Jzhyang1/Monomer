package systems.monomer.syntaxtree.operators;

import systems.monomer.syntaxtree.Node;

public class ThenNode extends OperatorNode {
    public ThenNode() {
        super("then");
    }

    @Override
    public Node matchTypes() {
        super.matchTypes();
        setType(get(size() - 1).getType());
        return this;
    }

    @Override
    public void setIsExpression(boolean isExpression) {
        super.setIsExpression(isExpression);
    }
}
