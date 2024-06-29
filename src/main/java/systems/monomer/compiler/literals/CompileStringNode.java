package systems.monomer.compiler.literals;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.Assembly.Register;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileNode;
import systems.monomer.compiler.CompileSize;
import systems.monomer.syntaxtree.literals.StringNode;

import java.nio.ByteBuffer;

public class CompileStringNode extends StringNode implements CompileNode {
    public CompileStringNode(String s) {
        super(s);
    }

    @Override
    public CompileSize compileSize() {
        return new CompileSize(value.length());
    }

    @Override
    public void compileVariables(AssemblyFile file) {
        throw syntaxError("Cannot compile string value as variable");
    }

    public Operand compileValue(AssemblyFile file) {
        //pushing on the 8-byte blocks of the string
        //traverses in reverse order because stack grows downwards
        for(int i = value.length(); i >= 0; i -= 8) {
            String sub = value.substring(Math.max(i-8, 0), i);
            file.push(new Operand(Operand.Type.IMMEDIATE,
                    null,
                    0,
                    ByteBuffer.wrap(sub.getBytes()).getInt()));
        }
        file.push(new Operand(Operand.Type.IMMEDIATE,
                null,
                0,
                value.length()));

        //TODO I think this is right
        return new Operand(Operand.Type.MEMORY,
                Register.ESP,
                0,
                0);
    }
}
