package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.CompileMemory;
import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.syntaxtree.OperatorNode;

public class AssignOperatorNode extends OperatorNode {
    public AssignOperatorNode() {
        super("=");
    }

    public void matchTypes() {
        throw new Error("TODO unimplemented");
    }

    public InterpretVariable interpretVariable() {
        throw new Error("TODO unimplemented");
    }
    public InterpretValue interpretValue() {
        return null;
    }

    public CompileMemory compileMemory() {
        throw new Error("TODO unimplemented");
    }
    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }
    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
