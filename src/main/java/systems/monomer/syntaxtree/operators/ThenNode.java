package systems.monomer.syntaxtree.operators;

public class ThenNode extends OperatorNode {
    public ThenNode() {
        super("then");
    }

    @Override
    public void matchTypes() {
        super.matchTypes();
        setType(get(size() - 1).getType());
    }

    @Override
    public void setIsExpression(boolean isExpression) {
        super.setIsExpression(isExpression);
    }
}
