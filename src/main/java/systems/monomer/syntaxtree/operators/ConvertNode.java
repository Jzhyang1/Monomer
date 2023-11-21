package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretValue;

public class ConvertNode extends OperatorNode {

    public ConvertNode() {
        super("to");
    }

    public void matchTypes() {
        super.matchTypes();
        setType(getSecond().getType());
        getFirst().setType(getType());
    }

    public InterpretValue interpretValue() {
        throw new Error("TODO unimplemented");
    }

    public Operand compileValue(AssemblyFile file) {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
