package systems.monomer.syntaxtree.literals;

import systems.monomer.syntaxtree.Node;
import systems.monomer.types.Type;
import systems.monomer.types.tuple.TupleType;

import java.util.List;

import static systems.monomer.execution.Handler.init;

public class TupleNode extends LiteralNode {
    public static boolean isTuple(Node node) {
        //TODO this is ugly
        return node.getUsage() == Usage.LITERAL && List.of("block", ",", ";").contains(node.getName());
    }

    public static TupleNode asTuple(Node node) {
        return isTuple(node) ? (TupleNode) node : init.tupleNode(List.of(node));
    }

    @Override
    public void matchTypes() {
        super.matchTypes();
        setType(new TupleType(getChildren().stream().map((e) -> e.getType()).toList()));
    }

    public void setType(Type type) {
        super.setType(type);
        if (TupleType.EMPTY.typeContains(type)) {
            List<Node> nodeList = getChildren();
            for (int i = 0; i < nodeList.size(); i++) {
                nodeList.get(i).setType(((TupleType)type).get(i));
            }
        }
    }

    public TupleNode() {
        super(",");
    }

    public TupleNode(String name) {
        super(name);
    }
}
