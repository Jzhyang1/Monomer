package systems.monomer.syntaxTree;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretValue;

public class ModuleNode extends Node {
    public ModuleNode(String name) {
        super(name);
    }

    public Usage getUsage() {
        return Usage.MODULE;
    }

    public InterpretValue interpretValue() {
        throw new Error("TODO unimplemented");
    }

    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
