package systems.monomer.compiler.literals;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileNode;
import systems.monomer.compiler.CompileSize;
import systems.monomer.syntaxtree.literals.IntNode;

public class CompileIntNode extends IntNode implements CompileNode {

    public CompileIntNode(Integer i) {
        super(i);
    }

    @Override
    public Operand compileValue(AssemblyFile file) {
        return new Operand(Operand.Type.IMMEDIATE, null, 0, value);
    }

    @Override
    public CompileSize compileSize() {
        return new CompileSize(8);
    }

    @Override
    public void compileVariables(AssemblyFile file) {
        throw syntaxError("Cannot compile integer value as variable");
    }
}
