package systems.monomer.syntaxtree;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import systems.monomer.execution.Constants;
import systems.monomer.errorhandling.Context;
import systems.monomer.errorhandling.Index;
import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.tokenizer.Source;
import systems.monomer.types.pseudo.AnyType;
import systems.monomer.types.Type;
import systems.monomer.variables.Key;
import systems.monomer.variables.VariableKey;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

@Getter
public abstract class Node extends ErrorBlock<Node> {
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


    protected Node(String name) {
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
    protected final void set(int i, Node node) {
        children.set(i, node);
        node.setParent(this);
    }

    public final void add(Node node) {
        children.add(node);
        node.setParent(this);
    }
    public final Node with(Node node) {
        add(node);
        return this;
    }
    public final Node with(Collection<? extends Node> nodes) {
        addAll(nodes);
        return this;
    }
    public final Node with(Context context) {
        setContext(context);
        return this;
    }
    public final Node with(Index start, Index stop, Source source) {
        setContext(start, stop, source);
        return this;
    }
    public final Node with(Type newType) {
        setType(newType);
        return this;
    }

    public final void addAll(Collection<? extends Node> nodes) {
        for (Node child : nodes)
            add(child);
    }
    public final int size() {
        return children.size();
    }

    /**
     * sets the VariableKey of this node and all children of this node
     * in cases where a variable node is used.
     * Returns the node with the new VariableKey (usually _this_).
     * If a new node is created that is not _this_, the returned node will still
     * have the same parent and context as the original node (_this_).
     */
    public Node matchVariables() {
        children.replaceAll(Node::matchVariables);
        with(getContext()).setParent(parent);
        return this;
    }

    /**
     * sets the type of this node after matching the types of the children;
     * returns the node with the new type (usually _this_).
     * If a new node is created that is not _this_, the returned node will still
     * have the same parent and context as the original node (_this_).
     */
    public Node matchTypes() {
        children.replaceAll(Node::matchTypes);
        return this;
    }

    /**
     * sets whether this node is the destination of an assignment (ie the left side of an assignment).
     * This is used primarily in VariableNode. False by default.
     * @param isDestination true if this node is the destination of an assignment, false if this node is a value
     */
    public void setIsDestination(boolean isDestination) {
        for (Node child : children) {
            child.setIsDestination(isDestination);
        }
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

    /**
     * simplifies the node; this is where post-matching processes occurs
     * (i.e. storing the conversion function). In general, anything that
     * requires correct types (simplification, removal of extra nodes,
     * caching, etc) will be written here.
     * If a new node is created that is not _this_, the returned node will still
     * have the same parent and context as the original node (_this_).
     * @return usually this, but sometimes the simplified node
     */
    //@Forbid non-simplified type
    public Node simplify() {
        setType(getType().simplify());
        with(getContext()).setParent(parent);
        return this;
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
