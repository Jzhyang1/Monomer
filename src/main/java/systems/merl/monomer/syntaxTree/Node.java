package systems.merl.monomer.syntaxTree;

import systems.merl.monomer.compiler.CompileMemory;
import systems.merl.monomer.compiler.CompileSize;
import systems.merl.monomer.compiler.CompileValue;
import systems.merl.monomer.interpreter.InterpretValue;
import systems.merl.monomer.interpreter.InterpretVariable;
import systems.merl.monomer.variables.Type;
import systems.merl.monomer.variables.VariableKey;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Node {
    private String name;
    private Node parent;
    private List<Node> children = new ArrayList<>();

    private Map<String, VariableKey> variables = new HashMap();

    private Type type;

    public enum Usage {
        OPERATOR, LITERAL, IDENTIFIER, MODULE
    }

    public Node(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract Usage getUsage();

    public Node getParent() {
        return parent;
    }

    protected void setParent(Node node) {
        parent = node;
    }

    public abstract Type getType();

    public abstract void setType(Type type);

    public VariableKey getVariable(String name) {
        return parent.getVariable(name);
    }

    public void putVariable(String name, VariableKey key) {
        parent.putVariable(name, key);
    }

    public VariableKey getVariableKey() {
        throw new UnsupportedOperationException("Unsupported method getVariableKey in Node");
    }

    public List<Node> getChildren() {
        return this.children;
    }
    public Node get(int i) {
        return children.get(i);
    }
    public void add(Node node) {
        children.add(node);
        node.setParent(this);
    }

    public void locateVariables() {
        for (Node child : children) {
            child.locateVariables();
        }
    }

    public void matchVariables() {
        for (Node child : children) {
            child.matchVariables();
        }
    }

    public void matchTypes() {
        for (Node child : children) {
            child.matchTypes();
        }
    }

    public void matchOverloads() {
        for (Node child : children) {
            child.matchOverloads();
        }
    }

    public abstract InterpretVariable interpretVariable();

    public abstract InterpretValue interpretValue();

    public abstract CompileMemory compileMemory();

    public abstract CompileValue compileValue();

    public abstract CompileSize compileSize();
}
