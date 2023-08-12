package systems.monomer.syntaxtree.literals;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretTuple;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.Node;

import java.util.Collection;

public class TupleNode extends LiteralNode {

    public TupleNode() {
        super("tuple");
    }
    public TupleNode(Collection<? extends Node> list) {
        super("tuple");
        addAll(list);
    }

    public InterpretValue interpretValue() {
        return new InterpretTuple(getChildren());
    }

    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
