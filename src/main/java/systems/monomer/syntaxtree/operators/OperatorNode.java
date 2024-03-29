package systems.monomer.syntaxtree.operators;

import org.jetbrains.annotations.Nullable;
import systems.monomer.syntaxtree.Node;

import java.util.*;

public abstract class OperatorNode extends Node {
    public static boolean isOperator(Node node, @Nullable String name) {
        return node.getUsage() == Usage.OPERATOR && (name == null || node.getName().equals(name));
    }

    public OperatorNode(String name) {
        super(name);
    }

    public Node getFirst() {
        return get(0);
    }

    public Node getSecond() {
        return get(1);
    }

    public Usage getUsage() {
        return Usage.OPERATOR;
    }
}
