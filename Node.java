import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Node {
    private String name;
    private Node parent;
    private ArrayList<Node> children = new ArrayList<Node>();
    private Map variables = new HashMap();
    private LineContext context;
    private Usage usage;

    public enum Usage {
        OPERATOR, LITERAL, INDENTIFIER
    }

    public Node(String name, Usage usage, LineContext context) {
        this.name = name;
        this.usage = usage;
        this.context = context;
    }

    public String getName() {
        return name;
    }

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

    public void setVariable(String name, VariableKey key) {
        parent.setVariable(name, key);
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
