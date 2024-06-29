package systems.monomer.syntaxtree.literals;

import systems.monomer.syntaxtree.Node;

import java.util.Collection;

public class SetNode extends LiteralNode {

    public SetNode(){
        super("set");
    }

    public SetNode(Collection<Node> x) {
        super("set");
        getChildren().addAll(x);
    }
}
