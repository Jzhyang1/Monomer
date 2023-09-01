package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.Type;

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

    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }
    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
