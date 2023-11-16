package systems.monomer.syntaxtree.literals;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretNumber;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.NumberType;

import java.nio.ByteBuffer;

public class IntNode extends LiteralNode {
    private Integer value;

    public IntNode(Integer i) {
        super(i.toString());
        value = i;
    }

    @Override
    public void matchTypes() {
        setType(NumberType.INTEGER);
    }

    @Override
    public InterpretValue interpretValue() {
        return new InterpretNumber<>(value);
    }

    @Override
    public Operand compileValue(AssemblyFile file) {
        return new Operand(Operand.Type.IMMEDIATE, null, 0, value);
    }

    @Override
    public CompileSize compileSize() {
        return new CompileSize(8);
    }
}
