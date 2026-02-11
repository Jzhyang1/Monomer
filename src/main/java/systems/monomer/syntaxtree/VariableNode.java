package systems.monomer.syntaxtree;

import lombok.Getter;
import systems.monomer.types.Type;
import systems.monomer.types.pseudo.AnyType;
import systems.monomer.types.pseudo.PlaceholderType;
import systems.monomer.variables.VariableKey;

@Getter
public class VariableNode extends Node {
    protected VariableKey variableKey = null;
    private boolean isDestination = false;

    public VariableNode(String name) {
        super(name);
    }
    protected VariableNode(String name, VariableKey key) {
        super(name);
        variableKey = key;
    }

    public Usage getUsage() {
        return Usage.IDENTIFIER;
    }

    public Node matchVariables() {
        VariableKey existing = getVariable(getName());
        if (variableKey == null && existing == null) putVariable(getName(), variableKey = (VariableKey) env.variableKey());
        else if (existing == null) putVariable(getName(), variableKey);
        else variableKey = existing;

        if(isDestination) return this;
        return new VariableValueNode(getName(), variableKey).matchVariables();
    }


    public Node matchTypes() {
        //as of right now actualType is not a PlaceHolderType
        PlaceholderType placeholderType = variableKey.getType();
        Type expectedType = placeholderType.getExpressed();
        Type actualType = getType();

        if (expectedType == AnyType.ANY)
            placeholderType.setExpressed(actualType);
        else if (actualType != AnyType.ANY && !actualType.typeContains(expectedType))
            throw syntaxError("Expected type " + expectedType + ", got " + actualType);

        setType(placeholderType);
        return this;
    }

    public void setIsDestination(boolean isDestination) {
        this.isDestination = isDestination;
    }
}