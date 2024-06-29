package systems.monomer.syntaxtree.literals;

import systems.monomer.syntaxtree.Node;
import systems.monomer.types.TupleType;
import systems.monomer.types.Type;

import java.util.List;

import static systems.monomer.syntaxtree.Configuration.create;

public class TupleNode extends LiteralNode {
    public static TupleNode EMPTY = create().tupleNode();

    public static boolean isTuple(Node node) {
        //TODO this is ugly
        return node.getUsage() == Usage.LITERAL && List.of("block", ",", ";").contains(node.getName());
    }

    public static TupleNode asTuple(Node node) {
        return isTuple(node) ? (TupleNode) node : create().tupleNode(List.of(node));
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
                nodeList.get(i).setType(((TupleType)type).getType(i));
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
