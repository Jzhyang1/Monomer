package systems.monomer.syntaxtree.operators;

public class WithThenNode extends OperatorNode {
    protected final int valueIndex;
    public WithThenNode(String name) {
        super(name);
        valueIndex = "then".equals(name) ? 1 : 0;

        assert "with".equals(name) || "then".equals(name);
    }

    @Override
    public void matchTypes() {
        super.matchTypes();
        setType(get(valueIndex).getType());
    }

    @Override
    public void setIsExpression(boolean isExpression) {
        super.setIsExpression(isExpression);
    }
}
