package systems.monomer.syntaxtree;

import lombok.Getter;
import systems.monomer.types.Type;
import systems.monomer.variables.VariableKey;

import static systems.monomer.types.pseudo.AnyType.ANY;

@Getter
public class VariableNode extends Node {
    protected VariableKey variableKey = null;

    public VariableNode(String name) {
        super(name);
    }

    public Usage getUsage() {
        return Usage.IDENTIFIER;
    }

    public void matchVariables() {
        VariableKey existing = getVariable(getName());
        if (variableKey == null && existing == null)
            putVariable(getName(), variableKey = new VariableKey());
        else if (existing == null)
            putVariable(getName(), variableKey);
        else
            variableKey = existing;
    }

    public void matchTypes() {
        if (getType() == ANY)
            setType(variableKey.getType());
        else if(variableKey.getType() == ANY)
            variableKey.setType(getType());
        //TODO uncomment code below
//        else if(!key.getType().typeContains(getType()))
//            throwError("Type mismatch: " + getType() + " is not matchable to " + key.getType());
    }

    @Override
    public Type getType() {
        return variableKey == null ? ANY : variableKey.getType();
    }

    @Override
    public void setType(Type type) {
        if (variableKey == null) variableKey = new VariableKey();

        variableKey.setType(type);
    }
}