package systems.monomer.syntaxtree.operators;

import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.VariableNode;
import systems.monomer.types.Type;

import java.util.List;

//TODO move some things to DeclareNode
public class AssignNode extends OperatorNode {
    protected boolean toLock = true;

    public AssignNode() {
        super("=");
    }

    @Override
    public void setIsExpression(boolean isExpression) {
        setThisExpression(isExpression);

        getFirst().setIsExpression(isExpression);
        //the RHS children are all expressions
    }

    @Override
    public Node matchVariables() {
        Node first = getFirst(), second = getSecond();
        if (first instanceof CallNode callNode)
            return new DefinitionNode(callNode, second);


        first.setIsDestination(true);
        if(second instanceof AssertTypeNode assertTypeNode &&
                    assertTypeNode.getFirst() instanceof VariableNode variableNode &&
                    "var".equals(variableNode.getName())) {
            set(1, second = assertTypeNode.getSecond());
            first.matchVariables();
//            first.getVariableKey().setConstant(false); //TODO set variable(s) to non-constant
            second.matchVariables();
            toLock = false;
        } else {
            super.matchVariables();
        }
        return this;
    }

    public Node matchTypes() {//normal variable assignment
        List<Node> children = getChildren();
        Node value = children.get(children.size() - 1);
        value.matchTypes();
        Type valType = value.getType();

        for (int i = children.size() - 2; i >= 0; --i) {
            Node variable = children.get(i);

            Type variableType = variable.getType();
            variable.setType(variableType.testReplace(variableType.assign(valType)));
            variable.matchTypes();
        }
        setType(getFirst().getType());
        return this;
    }
}
