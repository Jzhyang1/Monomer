package systems.merl.monomer.syntaxTree;

import systems.merl.monomer.compiler.CompileSize;
import systems.merl.monomer.compiler.CompileValue;
import systems.merl.monomer.interpreter.InterpretBaseValue;
import systems.merl.monomer.interpreter.InterpretValue;

public class IntNode extends LiteralNode{

    private Integer value;

    public IntNode(Integer i) {
        super(i.toString());
        value = i;
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
