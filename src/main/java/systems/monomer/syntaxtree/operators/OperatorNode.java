package systems.monomer.syntaxtree.operators;

import systems.monomer.syntaxtree.Node;

import java.util.*;

public abstract class OperatorNode extends Node {
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

    //TODO probably make a ChainedOperatorNode and move this there
    private List<String> names = null;
    public void addName(String name) {
        if(names == null) {
            super.addName(name);
            names = new ArrayList<>();
        }
        names.add(name);
    }
}
