package systems.monomer.compiler.control;

import systems.monomer.compiler.assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileNode;
import systems.monomer.compiler.CompileSize;
import systems.monomer.syntaxtree.controls.ReturnNode;

public class CompileReturnNode extends ReturnNode implements CompileNode {
    @Override
    public Operand compileValue(AssemblyFile file) {
        throw new Error("TODO unimplemented");
    }

    @Override
    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }

    @Override
    public void compileVariables(AssemblyFile file) {
        throw syntaxError("Cannot compile return value as variable");
    }
}
