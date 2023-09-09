package systems.monomer.syntaxtree.operators;

import org.jetbrains.annotations.Nullable;
import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.variables.VariableKey;

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

    public InterpretResult interpretValue() {
        //TODO check that the type is a subtype of the type
        return getSecond().interpretValue();
    }

    @Override
    public InterpretVariable interpretVariable() {
        return getSecond().interpretVariable();
    }

    @Override
    public @Nullable VariableKey getVariableKey() {
        return getSecond().getVariableKey();
    }

    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }
    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
