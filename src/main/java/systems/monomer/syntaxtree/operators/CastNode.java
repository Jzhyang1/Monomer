package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.types.Type;

import static systems.monomer.types.AnyType.ANY;

/**
 * The first child is the value
 * The second child is the type
 */
public class CastNode extends OperatorNode {
    public CastNode() {
        super("as");
    }

    public void matchTypes() {
        super.matchTypes();
        if(getType() == ANY) setType(getSecond().getType());
        if(getSecond().getType() == ANY) getSecond().setType(getType());
        if(getType() != getSecond().getType()) throw syntaxError("Internal error casting simultaneously to " + getFirst().getType() + " and " + getSecond().getType());
        if(!getFirst().getType().typeContains(getSecond().getType())) throw syntaxError("Cannot cast " + getFirst().getType() + " to " + getSecond().getType());
    }

    public InterpretVariable interpretVariable() {
        throw new Error("TODO unimplemented");
    }
    public InterpretResult interpretValue() {
        throw new Error("TODO unimplemented");
    }

    public Operand compileValue(AssemblyFile file) {
        throw new Error("TODO unimplemented");
        //TODO idk how to do this
    }
    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
