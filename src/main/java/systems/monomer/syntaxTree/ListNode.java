package systems.monomer.syntaxTree;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretValue;

import java.util.Collection;

public class ListNode extends LiteralNode{

    public ListNode() {
        super("list");
    }

    public ListNode(Collection<? extends Node> list) {
        super("list");
        getChildren().addAll(list);
    }

    public InterpretValue interpretValue() {
        throw new Error("TODO unimplemented");
        // return new InterpretList(getChildren());
    }

    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
