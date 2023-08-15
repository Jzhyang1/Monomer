package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.CompileMemory;
import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.errorhandling.UnimplementedError;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.VariableNode;
import systems.monomer.variables.Type;
import systems.monomer.variables.VariableKey;

public final class FieldNode extends OperatorNode {
    private VariableKey key;

    public FieldNode(){
        super("field");
    }

    public VariableKey getVariableKey() {
        return key;
    }

    public void matchVariables() {
        getFirst().matchVariables();

        VariableKey parentKey = getFirst().getVariableKey();
        Node fieldNode = getSecond();
        if(!(fieldNode instanceof VariableNode)) {
            fieldNode.throwError("Expected variable name");
        }

        VariableNode field = (VariableNode)fieldNode;
        String fieldName = field.getName();
        VariableKey existing = parentKey.get(fieldName);
        if(existing == null) {
            existing = new VariableKey();
            parentKey.put(fieldName, existing);
        }

        key = existing;
    }

    public InterpretVariable interpretVariable() {
        return key;
    }
    public InterpretValue interpretValue() {
        return key.getValue();
    }

    public CompileMemory compileMemory() {
        throw new UnimplementedError();
    }
    public CompileValue compileValue() {
        throw new UnimplementedError();
    }
    public CompileSize compileSize() {
        throw new UnimplementedError();
    }
}
