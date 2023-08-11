package systems.monomer.syntaxtree;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretStringValue;
import systems.monomer.interpreter.InterpretValue;

public class StringNode extends LiteralNode{
    public static final Node EMPTY = new StringNode("");
    private String value;


    public StringNode(String s) {
        super("\"" + s + "\"");
        value = s;
    }

    @Override
    public InterpretValue interpretValue() {
        return new InterpretStringValue(value);
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
