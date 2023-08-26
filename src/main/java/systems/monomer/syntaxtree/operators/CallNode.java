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
        //TODO this is temporary
        if(getFirst() instanceof VariableNode variableNode) {
            String name = variableNode.getName();
            VariableKey existing = getVariable(name);

            if(existing == null) {
                putVariable(name, new FunctionKey());
            } else if (!(existing instanceof FunctionKey)) {
                throwError("Cannot call non-function");
            }

            super.matchVariables();
        }
    }

    @Override
    public FunctionKey getVariableKey() {
        return (FunctionKey)getFirst().getVariableKey();
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
