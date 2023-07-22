package systems.merl.monomer.syntaxTree;

import java.util.HashSet;
import java.util.Set;

public abstract class OperatorNode extends Node {
    public static Set<String> symbolOperators() {
        HashSet<String> operators = new HashSet<String>();
        return operators;
    }
    public static Set<String> symbolStartDelimiters() {
        HashSet<String> delimiters = new HashSet<>();
        return delimiters;
    }

    public OperatorNode(String name) {
        super(name);
    }

    public Node getFirst(){
        return get(0);
    }
    public Node getSecond(){
        return get(1);
    }

    public Usage getUsage() {
        return Usage.OPERATOR;
    }
}
