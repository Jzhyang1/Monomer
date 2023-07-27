package systems.monomer.syntaxTree;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretBaseValue;
import systems.monomer.interpreter.InterpretValue;

public class FloatNode extends LiteralNode{

    private Float value;

    public FloatNode(Float f) {
        super(f.toString());
        value = f;
    }

    @Override
    public InterpretValue interpretValue() {
        return new InterpretBaseValue<>(value);
    }

    @Override
    public CompileValue compileValue() {
        //TODO
        return null;
    }

    @Override
    public CompileSize compileSize() {
        //TODO
        return null;
    }
}
