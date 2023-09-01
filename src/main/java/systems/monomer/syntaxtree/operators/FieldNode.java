package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.CompileMemory;
import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.errorhandling.UnimplementedError;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.VariableNode;
import systems.monomer.variables.VariableKey;

public final class FieldNode extends OperatorNode {
    private class FieldKey {
        private final VariableKey key;
        private final String name;

        public FieldKey(String name , VariableKey key) {
            this.name = name;
            this.key = key;
        }

        public InterpretValue interpretValue() {
            return key == null ?
                    FieldNode.this.getFirst().interpretValue().get(name) :
                    key.getValue();
        }

        public VariableKey getVariableKey() {
            if (key == null) throwError("Attempting to access " + name + " as a variable");
            return key;
        }
    }
    private FieldKey key;

    public FieldNode(){
        super("field");
    }

    public VariableKey getVariableKey() {
        return key.getVariableKey();
    }

    public void matchVariables() {
        getFirst().matchVariables();

        Node fieldNode = getSecond();
        if(!(fieldNode instanceof VariableNode)) {
            fieldNode.throwError("Expected variable name");
        }
        VariableNode field = (VariableNode)fieldNode;
        String fieldName = field.getName();

        VariableKey parentKey = getFirst().getVariableKey();
        if(parentKey == null) {
            key = new FieldKey(fieldName, null);
        }
        else {
            VariableKey existing = parentKey.get(fieldName);
            if (existing == null) {
                existing = new VariableKey();
                parentKey.put(fieldName, existing);
            }

            key = new FieldKey(fieldName, existing);
        }
    }

    @Override
    public void matchTypes() {
        setType(key.key.getType());
    }

    public InterpretVariable interpretVariable() {
        return key.getVariableKey();
    }
    public InterpretValue interpretValue() {
        return key.interpretValue();
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
