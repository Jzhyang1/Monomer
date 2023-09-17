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
import systems.monomer.types.ObjectType;
import systems.monomer.types.Type;
import systems.monomer.variables.FieldKey;
import systems.monomer.variables.Key;
import systems.monomer.variables.VariableKey;

import static systems.monomer.types.AnyType.ANY;

public final class FieldNode extends OperatorNode {
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
        else
            variableKey = new FieldKey(fieldName, parentKey);
    }

    @Override
    public void matchTypes() {
        //add field to parent
        Type parentType = getFirst().getType();
        if(parentType == ANY)
            getFirst().setType(parentType = new ObjectType());
        if(!parentType.hasField(fieldName))
            parentType.setField(fieldName, super.getType());
        //if either this node's type or it's field's type are not set
        else if(getType() == ANY)
            setType(super.getType());
        else if(super.getType() == ANY)
            super.setType(getType());
    }

    @Override
    public Type getType() {
        return variableKey == null ? ANY : variableKey.getType();
    }

    @Override
    public void setType(Type type) {
        assert variableKey != null;

        variableKey.setType(type);
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
