package systems.monomer.syntaxtree.literals;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretNumber;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.NumberType;

import java.nio.ByteBuffer;

public class FloatNode extends LiteralNode {
    private final Double value;

    public FloatNode(Double f) {
        super(f.toString());
        value = f;
    }

    @Override
    public void matchTypes() {
        setType(NumberType.FLOAT);
    }

    @Override
    public InterpretValue interpretValue() {
        return new InterpretNumber<>(value);
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
}
