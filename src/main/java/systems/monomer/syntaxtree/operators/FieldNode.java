package systems.monomer.syntaxtree.operators;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.object.ObjectType;
import systems.monomer.types.Type;
import systems.monomer.variables.FieldKey;
import systems.monomer.variables.Key;

import static systems.monomer.types.pseudo.AnyType.ANY;

//TODO able to simplify into FieldNode and ArbitraryFieldNode
public class FieldNode extends OperatorNode {
    @Getter
    protected @Nullable FieldKey variableKey;
    protected String fieldName = null;

    public FieldNode(){
        super("field");
    }

    public Node matchVariables() {
        getFirst().matchVariables();

        Node fieldNode = getSecond();
        if(fieldNode.getUsage() == Usage.IDENTIFIER)
            fieldName = fieldNode.getName();
        else
            throw fieldNode.syntaxError("Expected variable name");

        Key parentKey = getFirst().getVariableKey();
        if(parentKey == null)
            variableKey = null;
        else
            //TODO use init.fieldKey
            variableKey = new FieldKey(fieldName, parentKey);
        return this;
    }

    @Override
    public Node matchTypes() {
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
        return this;
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
}
