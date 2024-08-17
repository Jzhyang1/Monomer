package systems.monomer.syntaxtree.operators;

public class WithNode extends OperatorNode {
    public WithNode() {
        super("with");
    }

    @Override
    public void matchTypes() {
        super.matchTypes();
        setType(getFirst().getType());
    }

    @Override
    public void setIsExpression(boolean isExpression) {
        super.setIsExpression(isExpression);
    }
}
