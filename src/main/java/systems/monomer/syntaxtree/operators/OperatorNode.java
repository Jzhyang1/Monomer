package systems.monomer.syntaxtree.operators;

import lombok.NonNull;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.controls.*;
import systems.monomer.syntaxtree.literals.TupleNode;

import static systems.monomer.syntaxtree.operators.Arithmetic.*;
import static systems.monomer.syntaxtree.operators.Bitwise.*;

import systems.monomer.util.Pair;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

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
