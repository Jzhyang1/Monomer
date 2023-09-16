package systems.monomer.syntaxtree.operators;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import systems.monomer.compiler.CompileMemory;
import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.errorhandling.UnimplementedError;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.VariableNode;
import systems.monomer.variables.FieldKey;
import systems.monomer.variables.Key;
import systems.monomer.variables.VariableKey;

import static systems.monomer.types.AnyType.ANY;

public final class FieldNode extends OperatorNode {
//    private class FieldKey {
//        private final VariableKey key;
//        private final String name;
//
//        public FieldKey(String name , VariableKey key) {
//            this.name = name;
//            this.key = key;
//        }
//
//        public InterpretValue interpretValue() {
//            if(key == null) {
//                InterpretResult first = FieldNode.this.getFirst().interpretValue();
//                if(!first.isValue()) {
//                    FieldNode.this.throwError("Attempting to access " + name + " as a variable");
//                }
//                return first.asValue().get(name);
//            }
//            else return key.getValue();
//        }
//
//        public VariableKey getVariableKey() {
//            if (key == null) throwError("Attempting to access " + name + " as a variable");
//            return key;
//        }
//    }
    @Getter
    private @Nullable FieldKey variableKey;
    private String fieldName = null;

    public FieldNode(){
        super("field");
    }

    public void matchVariables() {
        getFirst().matchVariables();

        Node fieldNode = getSecond();
        if(fieldNode instanceof VariableNode field)
            fieldName = field.getName();
        else
            fieldNode.throwError("Expected variable name");

        Key parentKey = getFirst().getVariableKey();
        if(parentKey == null)
            variableKey = null;
        else if (parentKey.hasField(fieldName))
            variableKey = new FieldKey(fieldName, parentKey);
        else {
            parentKey.put(fieldName, getType());
            variableKey = new FieldKey(fieldName, parentKey);
        }
    }

    @Override
    public void matchTypes() {
        if(variableKey != null) {
            if(getType() != ANY && !variableKey.getType().typeContains(getType()))
                throwError("Cannot assign " + variableKey.getType() + " to " + getType());

            setType(variableKey.getType());
        }
    }

    public InterpretVariable interpretVariable() {
        return variableKey;
    }
    public InterpretValue interpretValue() {
        if(variableKey == null) {
            InterpretResult first = getFirst().interpretValue();
            if(!first.isValue()) {
                throwError("Attempting to access " + fieldName + " as a variable");
            }
            return first.asValue().get(fieldName);
        }
        else
            return variableKey.getValue();
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
