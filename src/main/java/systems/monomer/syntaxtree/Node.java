package systems.monomer.syntaxtree;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import systems.monomer.Constants;
import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.errorhandling.Context;
import systems.monomer.errorhandling.Index;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.tokenizer.Source;
import systems.monomer.types.AnyType;
import systems.monomer.types.Type;
import systems.monomer.variables.Key;
import systems.monomer.variables.VariableKey;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

@Getter
public abstract class Node extends ErrorBlock {
    public enum Usage {
        OPERATOR, LITERAL, IDENTIFIER, LABEL, CONTROL_GROUP, MODULE
    }

    private String name;
    @Setter
    private Node parent;
    @Getter
    private final List<Node> children = new ArrayList<>();
    @Getter
    @Setter
    private Type type = AnyType.ANY;

    @Setter
    private boolean isThisExpression = true;


    public Node(String name) {
        addName(name);
    }

    public void addName(String newName) {
        this.name = newName;
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

    public boolean isOperator() {
        return getUsage() == Usage.OPERATOR;
    }
    public boolean isOperator(String name) {
        return isOperator() && this.name.equals(name);
    }
    public boolean isTuple() {
        return TupleNode.isTuple(this);
    }
    public boolean isControl() {
        return getUsage() == Usage.CONTROL_GROUP || getUsage() == Usage.LABEL;
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

    public void matchTypes() {
        for (Node child : children) {
            child.matchTypes();
        }
    }

    public void setIsExpression(boolean isExpression) {
        this.isThisExpression = isExpression;
        for (Node child : children) {
            child.setIsExpression(isExpression);
        }
    }

    public InterpretVariable interpretVariable() {
        throw syntaxError("Attempting to access " + name + " as a variable");
    }

    public abstract InterpretResult interpretValue();

    public void interpretAssign(InterpretValue value) {
        Key variableKey = getVariableKey();
        if(variableKey == null) {
            throw syntaxError("Attempting to assign to " + name + " as a variable");
        }
        getVariableKey().setType(value.getType());   //TODO this is a hack to get overloads to transfer over functions because they are stored as a type
        interpretVariable().setValue(value);
    }

    protected InterpretResult checkedResult(InterpretResult result) {
        if(!result.isValue()) return result;

        InterpretValue value = result.asValue();
        if(getType() == AnyType.ANY) return result;
        if(value.getType().typeContains(getType())) return result;
        return result;  //TODO this is just here for production
//        else throw syntaxError("Internal error (please report as bug) " + value.getType() + " received when expecting " + getType());
    }

    public abstract Operand compileValue(AssemblyFile file);

    public abstract CompileSize compileSize();

    public void compileVariables(AssemblyFile file) {
        for (Node child : children) {
            child.compileVariables(file);
        }
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
