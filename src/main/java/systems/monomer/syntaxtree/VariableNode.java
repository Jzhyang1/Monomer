package systems.monomer.syntaxtree;

import systems.monomer.compiler.CompileMemory;
import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.types.AnyType;
import systems.monomer.types.Type;
import systems.monomer.variables.VariableKey;

import static systems.monomer.types.AnyType.ANY;

public class VariableNode extends Node {
    private VariableKey key;

    public VariableNode(String name) {
        super(name);
    }
    public Usage getUsage() {
        return Usage.IDENTIFIER;
    }

    public void matchVariables() {
        VariableKey existing = getVariable(getName());
        if(key == null && existing == null)
            putVariable(getName(), key = new VariableKey());
        else if(existing == null)
            putVariable(getName(), key);
        else
            key = existing;
    }

    public void matchTypes() {
        if(getType() == ANY)  //TODO uncomment code below
            setType(key.getType());
        else // if(key.getType() == ANY)
            key.setType(getType());
//        else if(!key.getType().typeContains(getType()))
//            throwError("Type mismatch: " + getType() + " is not matchable to " + key.getType());
    }

    @Override
    public Type getType() {
        return key == null ? ANY : key.getType();
    }

    @Override
    public void setType(Type type) {
        if(key == null) key = new VariableKey();

        key.setType(type);
    }

    @SuppressWarnings("SuspiciousGetterSetter")
    public VariableKey getVariableKey() {
        return key;
    }

    public InterpretVariable interpretVariable() {
        return key;
    }
    public InterpretValue interpretValue() {
        return key.getValue();
    }

    public CompileMemory compileMemory() {
        throw new Error("TODO unimplemented");
    }
    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }
    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}