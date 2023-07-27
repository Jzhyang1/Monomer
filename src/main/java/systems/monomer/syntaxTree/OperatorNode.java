package systems.monomer.syntaxTree;

import java.util.HashSet;
import java.util.Set;

public abstract class OperatorNode extends Node {
    public static Set<String> symbolOperators() {
        HashSet<String> operators = new HashSet<>();  //TODO
        return operators;
    }
    public static Set<String> symbolStartDelimiters() {
        HashSet<String> delimiters = new HashSet<>();   //TODO
        return delimiters;
    }

    public static boolean isBreaking(String nextToken) {
        return true;    //TODO
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
