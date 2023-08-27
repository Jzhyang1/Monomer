package systems.monomer.syntaxtree.literals;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretTuple;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.TupleType;

import java.util.Collection;
import java.util.List;

public class TupleNode extends LiteralNode {
    public static boolean isTuple(Node node) {
        //TODO this is ugly
        return node.getUsage() == Usage.LITERAL && List.of("block", ",", ";").contains(node.getName());
    }

    public static TupleNode asTuple(Node node) {
        return isTuple(node) ? (TupleNode)node : new TupleNode(List.of(node));
    }

    @Override
    public TupleType getType() {
        return new TupleType(getChildren().stream().map((e)->e.getType()).toList());
    }

    public TupleNode() {
        super("block");
    }
    public TupleNode(String name) {
        super(name);
    }
    public TupleNode(Collection<? extends Node> list) {
        super("block");
        addAll(list);
    }

    public InterpretValue interpretValue() {
        return InterpretTuple.toTuple(getChildren());
    }

    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
