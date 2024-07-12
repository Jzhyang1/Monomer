package systems.monomer.compiler.literals;

import systems.monomer.compiler.assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileNode;
import systems.monomer.compiler.CompileSize;
import systems.monomer.syntaxtree.literals.FloatNode;

import java.nio.ByteBuffer;

public class CompileFloatNode extends FloatNode implements CompileNode {
        public CompileFloatNode(Double f) {
            super(f);
        }

    @Override
    public Operand compileValue(AssemblyFile file) {
        return new Operand(Operand.Type.IMMEDIATE,
                null,
                0,
                (int) ByteBuffer.allocate(8).putDouble(value).rewind().getLong());
    }

    @Override
    public CompileSize compileSize() {
        return new CompileSize(8);
    }

    @Override
    public void compileVariables(AssemblyFile file) {
        throw syntaxError("Cannot compile float value as variable");
    }
}
