package systems.monomer.syntaxTree;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretBaseValue;
import systems.monomer.interpreter.InterpretValue;

public class CharNode extends LiteralNode{

    private Character value;

    public CharNode(Character c) {
        super(c.toString());
    }

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
