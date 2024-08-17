package systems.monomer.syntaxtree;

import lombok.Getter;
import systems.monomer.execution.Handler;
import systems.monomer.types.Type;
import systems.monomer.types.pseudo.PlaceholderType;
import systems.monomer.variables.Key;
import systems.monomer.variables.VariableKey;

import static systems.monomer.types.pseudo.AnyType.ANY;

@Getter
public class VariableNode extends Node {
    protected VariableKey variableKey = null;
    private boolean isDestination = false;

    public VariableNode(String name) {
        super(name);
    }

    public Usage getUsage() {
        return Usage.IDENTIFIER;
    }

    public void matchVariables() {
        VariableKey existing = getVariable(getName());
        if (variableKey == null && existing == null)
            putVariable(getName(), variableKey = Handler.init.variableKey());
        else if (existing == null)
            putVariable(getName(), variableKey);
        else
            variableKey = existing;
    }

    public void matchTypes() {
        //handles multiple occurrences of VariableNode
//        if(isDestination)
//            setType(variableKey.getType());
//        else
//            setType(variableKey.getType().getExpressed());

//        if (getType() == ANY)
//            setType(variableKey.getType());
//        else if(variableKey.getType() == ANY)
//            variableKey.setType(getType());
    }

    public void setType(Type type) {
        assert variableKey.getType().getExpressed().typeContains(type);

        super.setType(type);
        variableKey.getType().setExpressed(type);
    }

    @Override
    public Type getType() {
        if(isDestination) return variableKey.getType();
        else return variableKey.getType().getExpressed();
    }

    public void setIsDestination(boolean isDestination) {
        this.isDestination = isDestination;
    }

    @Override
    public Node simplify() {
        Type simplified = getType().simplify();
        setType(simplified);
        return this;
    }
}