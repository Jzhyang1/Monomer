package systems.monomer.syntaxtree.literals;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretBool;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.BoolType;

public class BoolNode extends LiteralNode {
    private final boolean value;

    public BoolNode(boolean value){
        super("bool");
        this.value = value;
    }

    @Override
    public void matchTypes() {
        setType(BoolType.BOOL);
    }

    public InterpretValue interpretValue() {
        return new InterpretBool(value);
    }

    public Operand compileValue(AssemblyFile file) {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
