package systems.merl.monomer.syntaxTree;

import systems.merl.monomer.compiler.CompileMemory;
import systems.merl.monomer.compiler.CompileSize;
import systems.merl.monomer.compiler.CompileValue;
import systems.merl.monomer.errorHandling.ErrorBlock;
import systems.merl.monomer.interpreter.InterpretValue;
import systems.merl.monomer.interpreter.InterpretVariable;
import systems.merl.monomer.variables.Type;
import systems.merl.monomer.variables.VariableKey;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Node extends ErrorBlock{
    private String name;
    private Node parent;
    private List<Node> children = new ArrayList<>();

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public VariableKey getVariable(String name) {
        return parent.getVariable(name);
    }

    public void putVariable(String name, VariableKey key) {
        parent.putVariable(name, key);
    }

    public VariableKey getVariableKey(){
        throwError("Attempting to access " + name + " as a variable");
        return null;
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

    public InterpretVariable interpretVariable() {
        throwError("Attempting to access " + name + " as a variable");
        return null;
    }

    public abstract InterpretValue interpretValue();

    public CompileMemory compileMemory() {
        throwError("Attempting to access " + name + " as a variable");
        return null;
    }

    public abstract CompileValue compileValue();

    public abstract CompileSize compileSize();
}
