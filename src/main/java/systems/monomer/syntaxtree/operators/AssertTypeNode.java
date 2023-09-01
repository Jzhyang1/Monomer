package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretValue;

public class AssertTypeNode extends OperatorNode {

    public AssertTypeNode() {
        super(":");
    }

    public void matchTypes() {
        getFirst().matchTypes();
        setType(getFirst().getType());
        getSecond().setType(getType());
        getSecond().matchTypes();
    }

    public InterpretValue interpretValue() {
        //TODO check that the type is a subtype of the type
        return getSecond().interpretValue();
    }

    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }
    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
