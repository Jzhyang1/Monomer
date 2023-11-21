package systems.monomer.syntaxtree.literals;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.Assembly.Register;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretTuple;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.TupleType;
import systems.monomer.types.Type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TupleNode extends LiteralNode {
    public static TupleNode EMPTY = new TupleNode(List.of());

    public static boolean isTuple(Node node) {
        //TODO this is ugly
        return node.getUsage() == Usage.LITERAL && List.of("block", ",", ";").contains(node.getName());
    }

    public static TupleNode asTuple(Node node) {
        return isTuple(node) ? (TupleNode) node : new TupleNode(List.of(node));
    }

    @Override
    public void matchTypes() {
        super.matchTypes();
        setType(new TupleType(getChildren().stream().map((e) -> e.getType()).toList()));
    }

    public void setType(Type type) {
        super.setType(type);
        if (type instanceof TupleType tupleType) {
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

    public InterpretResult interpretValue() {
        List<InterpretValue> ret = new ArrayList<>();
        for (Node child : getChildren()) {
            InterpretResult result = child.interpretValue();
            if (!result.isValue()) return result;
            ret.add(result.asValue());
        }
        return new InterpretTuple(ret);
    }

    public Operand compileValue(AssemblyFile file) {
        for (Node child : getChildren()) {
            file.push(child.compileValue(file));
        }
        return new Operand(Operand.Type.MEMORY,
                Register.ESP,
                0,
                0);
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
