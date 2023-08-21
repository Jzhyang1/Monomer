package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretFunction;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.VariableNode;
import systems.monomer.variables.FunctionKey;
import systems.monomer.types.Type;

public class CallNode extends OperatorNode {
    public CallNode() {
        super("call");
    }

    public Type getSignature() {
        return new InterpretFunction(getType(), getSecond().getType());
    }

    @Override
    public void matchVariables() {
        //TODO this is temporary
        if(getFirst() instanceof VariableNode variableNode) {
            String name = variableNode.getName();
            putVariable(name, new FunctionKey());
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
