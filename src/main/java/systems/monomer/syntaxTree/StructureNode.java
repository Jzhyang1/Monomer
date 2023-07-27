package systems.monomer.syntaxTree;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.variables.VariableKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructureNode extends LiteralNode {
    private Map<String, VariableKey> variables = new HashMap<>();

    public StructureNode(){}
    public StructureNode(List<Node> children) {
        children.stream().forEach((child) -> add(child));
    }

    public void putVariable(String name, VariableKey key) {
        variables.put(name, key);
    }
    public VariableKey getVariable(String name) {
        throw new Error("TODO unimplemented");
    }

    public void matchTypes() {
        //TODO set type to generated type copy of self
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
