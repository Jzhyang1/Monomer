package systems.monomer.syntaxTree;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretSet;
import systems.monomer.interpreter.InterpretValue;

import java.util.Collection;

public class SetNode extends LiteralNode{

    public SetNode(){
        super("set");
    }

    public SetNode(Collection<Node> x) {
        super("set");
        getChildren().addAll(x);
    }

    public InterpretValue interpretValue() {
        throw new Error("TODO unimplemented");
        return new InterpretSet(getChildren().stream().map(Node::interpretValue).toList());
    }

    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
