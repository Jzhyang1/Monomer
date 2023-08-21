package systems.monomer.syntaxtree;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretTuple;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.variables.VariableKey;

import java.util.HashMap;
import java.util.Map;

public class ModuleNode extends Node {
    public ModuleNode(String name) {
        super(name);
    }

    public Usage getUsage() {
        return Usage.MODULE;
    }

    //variables map and overloaded getVariable to access the variables
    private Map<String, VariableKey> variables = new HashMap<>();
    public void putVariable(String name, VariableKey key) {
        variables.put(name, key);
    }
    public VariableKey getVariable(String name) {
        return variables.get(name);
    }

    public InterpretValue interpretValue() {
        return InterpretTuple.toTuple(getChildren());   //TODO replace Tuple with Module
    }

    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
