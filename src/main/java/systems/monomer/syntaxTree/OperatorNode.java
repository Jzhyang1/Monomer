package systems.monomer.syntaxTree;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class OperatorNode extends Node {
    public static Set<String> symbolOperators() {
        HashSet<String> operators = new HashSet<>(List.of("=", "+", "-", ";", ">", "<"));  //TODO
        return operators;
    }
    public static Set<Character> symbolStartDelimiters() {
        HashSet<Character> delimiters = new HashSet<>(List.of('(', '[', '{'));   //TODO
        return delimiters;
    }
    public static Set<Character> symbolEndDelimiters() {
        HashSet<Character> delimiters = new HashSet<>(List.of(')', ']', '}'));   //TODO
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
