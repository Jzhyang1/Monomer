package systems.monomer.syntaxtree.literals;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretChar;
import systems.monomer.interpreter.InterpretValue;

public class CharNode extends LiteralNode {
    Character value;
    public CharNode(Character c) {
        super("char");
        value = c;
    }

    public InterpretValue interpretValue() {
        return new InterpretChar(value);
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
