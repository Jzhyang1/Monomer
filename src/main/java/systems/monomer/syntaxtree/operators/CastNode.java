package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.CompileMemory;
import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.types.Type;

/**
 * The first child is the value
 * The second child is the type
 */
public class CastNode extends OperatorNode {
    public CastNode() {
        super("as");
    }

    public Type getType() {
        return getSecond().getType();
    }

    public InterpretVariable interpretVariable() {
        throw new Error("TODO unimplemented");
    }
    public InterpretValue interpretValue() {
        throw new Error("TODO unimplemented");
    }

    public CompileMemory compileMemory() {
        return getFirst().compileMemory();
    }
    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }
    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
