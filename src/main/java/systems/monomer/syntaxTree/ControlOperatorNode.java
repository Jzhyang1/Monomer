package systems.monomer.syntaxTree;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.variables.VariableKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ControlOperatorNode extends OperatorNode{
    private Map<String, VariableKey> variables = new HashMap<>();

    public ControlOperatorNode(String name){
        super(name);
    }

    public void putVariable(String name, VariableKey key) {
        variables.put(name, key);
    }
    public VariableKey getVariable(String name) {
        throw new Error("TODO unimplemented");
    }

    public void matchTypes() {
        //TODO set type to Sequence if loop. If if condition, set type to whatever it evaluates to
        throw new Error("TODO unimplemented");
    }

    public InterpretValue interpretValue() {
        throw new Error("TODO unimplemented");
    }

    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
