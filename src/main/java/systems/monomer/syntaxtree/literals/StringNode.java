package systems.monomer.syntaxtree.literals;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.Assembly.Register;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretString;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.StringType;

import java.nio.ByteBuffer;

public class StringNode extends LiteralNode {
    public static final Node EMPTY = new StringNode("");
    private final String value;


    public StringNode(String s) {
        super("string");
        value = s;
    }

    @Override
    public void matchTypes() {
        setType(StringType.STRING);
    }

    @Override
    public InterpretValue interpretValue() {
        return new InterpretString(value);
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

    @Override
    public CompileSize compileSize() {
        return new CompileSize(value.length());
    }

    @Override
    public String toString(int indention) {
        return super.toString(indention) + " \"" + value + "\"";
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}
