package systems.monomer.syntaxTree;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretNumberValue;
import systems.monomer.interpreter.InterpretValue;

public class FloatNode extends LiteralNode{

    private Float value;

    public FloatNode(Float f) {
        super(f.toString());
        value = f;
    }

    @Override
    public InterpretValue interpretValue() {
        return new InterpretNumberValue<>(value);
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
