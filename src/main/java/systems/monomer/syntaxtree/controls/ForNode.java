package systems.monomer.syntaxtree.controls;
import systems.monomer.interpreter.values.InterpretSequence;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.operators.OperatorNode;
import systems.monomer.types.plural.CollectionType;
import systems.monomer.types.Type;
import systems.monomer.variables.Key;

public class ForNode extends ControlOperatorNode {
    protected Key iteratorKey;

    public ForNode() {
        super("for");
    }

    public void matchTypes() {
        OperatorNode firstNode = (OperatorNode) getFirst();
        firstNode.matchTypes();
        Node vars = firstNode.getFirst();
        Node collection = firstNode.getSecond();

        Type maybeCollectionType = collection.getType();
        if(maybeCollectionType instanceof CollectionType collectionType) {
            vars.setType(collectionType.getElementType());  //TODO handle multiple vars
        } else {
            throw firstNode.syntaxError("For loop requires a collection for the control of repetitions, received " + maybeCollectionType + " instead");
        }

        Node secondNode = getSecond();
        secondNode.matchTypes();
        setType(new InterpretSequence(secondNode.getType()));
    }

    public void matchVariables() {
        super.matchVariables();

        Node firstNode = getFirst();
        if(firstNode.getUsage() != Usage.OPERATOR &&
                !"in".equals(firstNode.getName()))
            throw syntaxError("For loop should be formatted as 'for <variable> in <collection>'");
        iteratorKey = ((OperatorNode) firstNode).getFirst().getVariableKey();
    }
}
