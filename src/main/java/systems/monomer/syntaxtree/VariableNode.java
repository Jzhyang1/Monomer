package systems.monomer.syntaxtree;

import systems.monomer.compiler.CompileMemory;
import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.variables.Type;
import systems.monomer.variables.VariableKey;

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
        if(existing == null) {
            existing = new VariableKey();
            putVariable(getName(), existing);
        }
        key = existing;
    }
    public void setType(Type type) {
        super.setType(type);
        key.setValue((InterpretValue) type);    //TODO this is a hack
    }

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