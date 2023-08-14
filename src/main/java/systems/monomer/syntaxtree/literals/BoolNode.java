package systems.monomer.syntaxtree.literals;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretBool;
import systems.monomer.interpreter.InterpretValue;

public class BoolNode extends LiteralNode {
    private boolean value;

    public BoolNode(boolean value){
        super("bool");
        this.value = value;
    }

    public InterpretValue interpretValue() {
        return new InterpretBool(value);
    }

    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
