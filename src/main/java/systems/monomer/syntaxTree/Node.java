package systems.monomer.syntaxTree;

import lombok.Getter;
import lombok.Setter;
import systems.monomer.compiler.CompileMemory;
import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.errorHandling.ErrorBlock;
import systems.monomer.variables.Type;
import systems.monomer.variables.VariableKey;

import java.util.List;
import java.util.ArrayList;

public abstract class Node extends ErrorBlock{
    public enum Usage {
        OPERATOR, LITERAL, IDENTIFIER, MODULE
    }

    private String name;
    @Getter @Setter
    private Node parent;
    @Getter
    private List<Node> children = new ArrayList<>();
    @Getter @Setter
    private Type type;


    public Node(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract Usage getUsage();

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

    public Node get(int i) {
        return children.get(i);
    }
    public void add(Node node) {
        children.add(node);
        node.setParent(this);
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
