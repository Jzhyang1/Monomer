package systems.merl.monomer.syntaxTree;

import systems.merl.monomer.compiler.CompileMemory;
import systems.merl.monomer.compiler.CompileSize;
import systems.merl.monomer.compiler.CompileValue;
import systems.merl.monomer.interpreter.InterpretBaseValue;
import systems.merl.monomer.interpreter.InterpretValue;
import systems.merl.monomer.interpreter.InterpretVariable;
import systems.merl.monomer.variables.Type;

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
