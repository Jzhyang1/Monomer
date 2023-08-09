package systems.monomer.syntaxTree;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretValue;

public class ControlGroupNode extends OperatorNode {
    public ControlGroupNode(){
        super("control");
    }

    public Usage getUsage() {
        return Usage.CONTROL_GROUP;
    }

    public void matchTypes() {
        throw new Error("TODO unimplemented");
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
