package systems.monomer.syntaxtree.literals;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretNumber;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.NumberType;

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
