package systems.monomer.syntaxTree;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretTuple;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.variables.Type;

import java.util.Collection;

public class TupleNode extends LiteralNode{

    public TupleNode() {
        super("tuple");
    }

    public TupleNode(Collection<? extends Node> list) {
        super("tuple");
        getChildren().addAll(list);
    }

    public InterpretValue interpretValue() {
        throw new Error("TODO unimplemented");
        //  new InterpretTuple(getChildren());
    }

    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
