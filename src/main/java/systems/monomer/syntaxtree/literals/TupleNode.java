package systems.monomer.syntaxtree.literals;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretTuple;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.AnyType;
import systems.monomer.types.TupleType;
import systems.monomer.types.Type;

import java.util.Collection;
import java.util.List;

public class TupleNode extends LiteralNode {
    public static TupleNode EMPTY = new TupleNode(List.of());
    public static boolean isTuple(Node node) {
        //TODO this is ugly
        return node.getUsage() == Usage.LITERAL && List.of("block", ",", ";").contains(node.getName());
    }

    public static TupleNode asTuple(Node node) {
        return isTuple(node) ? (TupleNode)node : new TupleNode(List.of(node));
    }

    @Override
    public void matchTypes() {
        super.matchTypes();
        setType(new TupleType(getChildren().stream().map((e)->e.getType()).toList()));
    }

    public void setType(Type type) {
        super.setType(type);
        if(type instanceof TupleType tupleType) {
            List<Node> nodeList = getChildren();
            for (int i = 0; i < nodeList.size(); i++) {
                nodeList.get(i).setType(tupleType.getType(i));
            }
        }
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
