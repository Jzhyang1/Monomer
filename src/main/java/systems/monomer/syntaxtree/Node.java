package systems.monomer.syntaxtree;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import systems.monomer.execution.Constants;
import systems.monomer.errorhandling.Context;
import systems.monomer.errorhandling.Index;
import systems.monomer.execution.Initializer;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.interpreter.Interpreter;
import systems.monomer.tokenizer.Source;
import systems.monomer.types.pseudo.AnyType;
import systems.monomer.types.Type;
import systems.monomer.variables.Key;
import systems.monomer.variables.VariableKey;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

@Getter
public abstract class Node extends ErrorBlock {
    public static Initializer init = new Interpreter();

    public enum Usage {
        OPERATOR, LITERAL, IDENTIFIER, LABEL, CONTROL_GROUP, MODULE
    }

    private final String name;
    @Setter
    private Node parent;
    @Getter
    private final List<Node> children = new ArrayList<>();
    @Getter
    @Setter
    private Type type = AnyType.ANY;

    /**
     * Whether the value of this node is used.
     * Used for optimization purposes to determine
     * whether to compute the value of this node.
     */
    @Setter
    private boolean isThisExpression = true;

    @Setter
    private boolean isVolatile = false;


    public Node(String name) {
        this.name = name;
    }

    public abstract Usage getUsage();

    public VariableKey getVariable(String varName) {
        return parent.getVariable(varName);
    }
    public void putVariable(String varName, VariableKey key) {
        parent.putVariable(varName, key);
    }

    /**
     * this returns null if not variable. To be used only in checking stages of compilation
     * @return the variable key
     */
    public @Nullable Key getVariableKey() {
        return null;
    }

    public Node get(int i) {
        return children.get(i);
    }
    protected void set(int i, Node node) {
        children.set(i, node);
    }

    public void add(Node node) {
        children.add(node);
        node.setParent(this);
    }
    public Node with(Node node) {
        add(node);
        return this;
    }
    public Node with(Collection<? extends Node> children) {
        addAll(children);
        return this;
    }
    public Node with(Context context) {
        setContext(context);
        return this;
    }
    public Node with(Index start, Index stop, Source source) {
        setContext(start, stop, source);
        return this;
    }
    public Node with(Type type) {
        setType(type);
        return this;
    }

    public final void addAll(Collection<? extends Node> children) {
        for (Node child : children)
            add(child);
    }
    public final int size() {
        return children.size();
    }

    public void matchVariables() {
        for (Node child : children) {
            child.matchVariables();
        }
    }

    /**
     * sets the type of this node after
     * matching the types of the children
     */
    public void matchTypes() {
        for (Node child : children) {
            child.matchTypes();
        }
    }

    /**
     * Finds the type of this node under the given context.
     * Usually this is the same as getType(), but if the type is incomplete
     * (i.e. it is of type ANY in a function), this will help resolve the type.
     * No side-effects should be performed in this method.
     * @param context variables in scope
     * @return the type of this node
     */
    public Type testType(TypeContext context) {
        return getType();
    }


    /**
     * True if the value of this node is used (determines
     * if the node only represents a side-effect)
     * @param isExpression whether this node is an expression or not
     */
    public void setIsExpression(boolean isExpression) {
        this.isThisExpression = isExpression;
        for (Node child : children) {
            child.setIsExpression(isExpression);
        }
    }

    protected InterpretResult checkedResult(InterpretResult result) {
        if(!result.isValue()) return result;

        InterpretValue value = result.asValue();
        if(getType() == AnyType.ANY) return result;
        if(value.getType().typeContains(getType())) return result;
        return result;  //TODO this is just here for production
//        else throw syntaxError("Internal error (please report as bug) " + value.getType() + " received when expecting " + getType());
    }

    protected String toString(int tabs) {
        StringBuilder ret = new StringBuilder();
        String tabString = Constants.TAB.repeat(tabs);
        ret.append(tabString).append(getUsage()).append(' ').append(name);
        if (!children.isEmpty()) {
            ret.append("[\n");
            for (Node child : children) {
                ret.append(child.toString(tabs + 1)).append('\n');
            }
            ret.append(tabString).append(']');
        }
        return ret.toString();
    }

    public String toString() {
        return toString(0);
    }
}
