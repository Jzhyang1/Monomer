package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretFunction;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.VariableNode;
import systems.monomer.types.Signature;
import systems.monomer.variables.FunctionKey;
import systems.monomer.types.Type;
import systems.monomer.variables.VariableKey;

public class CallNode extends OperatorNode {
    public CallNode() {
        super("call");
    }

    public Type getSignature() {
        return new Signature(getType(), getSecond().getType());
    }

    @Override
    public void matchVariables() {
        super.matchVariables();
        VariableKey existing = getFirst().getVariableKey();
        assert existing != null;

        if (existing.getValue() != null && !(existing.getValue() instanceof FunctionKey)) {
            throwError("Cannot call non-function");
        } else {
            existing = new VariableKey();
            existing.setValue(new FunctionKey());
//                existing.setType(new Signature(getType(), getSecond().getType()));
        }
    }

    public InterpretValue interpretValue() {
        return getFirst().interpretValue().call(getSecond().interpretValue());
    }

    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
