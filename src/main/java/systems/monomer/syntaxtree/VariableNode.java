package systems.monomer.syntaxtree;

import lombok.Getter;
import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.types.Type;
import systems.monomer.variables.VariableKey;

import static systems.monomer.types.AnyType.ANY;

@Getter
public class VariableNode extends Node {
    private VariableKey variableKey;

    public VariableNode(String name) {
        super(name);
    }
    public Usage getUsage() {
        return Usage.IDENTIFIER;
    }

    public void matchVariables() {
        VariableKey existing = getVariable(getName());
        if(variableKey == null && existing == null)
            putVariable(getName(), variableKey = new VariableKey());
        else if(existing == null)
            putVariable(getName(), variableKey);
        else
            variableKey = existing;
    }

    public void matchTypes() {
        if(getType() == ANY)  //TODO uncomment code below
            setType(variableKey.getType());
        else // if(key.getType() == ANY)
            variableKey.setType(getType());
//        else if(!key.getType().typeContains(getType()))
//            throwError("Type mismatch: " + getType() + " is not matchable to " + key.getType());
    }

    @Override
    public Type getType() {
        return variableKey == null ? ANY : variableKey.getType();
    }

    @Override
    public void setType(Type type) {
        if(variableKey == null) variableKey = new VariableKey();

        variableKey.setType(type);
    }

    public InterpretVariable interpretVariable() {
        return variableKey;
    }
    public InterpretValue interpretValue() {
        return variableKey.getValue();
    }

    public Operand compileValue(AssemblyFile file) {
        return variableKey.getAddress();
    }
    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}