package systems.monomer.syntaxtree.literals;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretChar;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.CharType;
import systems.monomer.types.NumberType;

public class CharNode extends LiteralNode {
    Character value;
    public CharNode(Character c) {
        super("char");
        value = c;
    }

    @Override
    public void matchTypes() {
        setType(CharType.CHAR);
    }

    public InterpretValue interpretValue() {
        return new InterpretChar(value);
    }

    @Override
    public Operand compileValue(AssemblyFile file) {
        return new Operand(Operand.Type.IMMEDIATE, null, 0, value.charValue());
    }
    @Override
    public CompileSize compileSize() {
        return new CompileSize(1);
    }
}
