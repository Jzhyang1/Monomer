package systems.merl.monomer.syntaxTree;

import systems.merl.monomer.compiler.CompileSize;
import systems.merl.monomer.compiler.CompileValue;
import systems.merl.monomer.interpreter.InterpretBaseValue;
import systems.merl.monomer.interpreter.InterpretValue;

public class StringNode extends LiteralNode{

    private String value;


    public StringNode(String s) {
        super("\"" + s + "\"");
        value = s;
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
