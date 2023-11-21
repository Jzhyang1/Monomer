package systems.monomer.syntaxtree;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretObject;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.literals.LiteralNode;
import systems.monomer.syntaxtree.operators.AssignNode;
import systems.monomer.types.ObjectType;
import systems.monomer.variables.VariableKey;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructureNode extends LiteralNode {
    public static final StructureNode EMPTY = new StructureNode();
    private final Map<String, VariableKey> variables = new HashMap<>();

    public StructureNode(){}
    public StructureNode(List<Node> children) {
        children.stream().forEach((child) -> add(child));
    }

    public void putVariable(String varName, VariableKey key) {
        variables.put(varName, key);
    }
    public VariableKey getVariable(String varName) {
        VariableKey ret = variables.get(varName);
        return ret == null ? getParent().getVariable(varName): ret;
    }
    public Collection<String> getFieldNames() {
        return variables.keySet();
    }

    public void matchTypes() {
        super.matchTypes();
        ObjectType ret = new ObjectType();
        for(Map.Entry<String, VariableKey> entry : variables.entrySet()) {
            ret.setField(entry.getKey(), entry.getValue().getType());
        }
        setType(ret);
    }

    public InterpretResult interpretValue() {
        for(Node child : getChildren()) {
            InterpretResult res = child.interpretValue();
            if(!res.isValue())
                return res;
        }

        InterpretObject ret = new InterpretObject();
        for(Map.Entry<String, VariableKey> entry : variables.entrySet()) {
            //TODO copy over function overloads
            ret.set(entry.getKey(), entry.getValue().getValue());
        }
        return ret;
    }

    @Override
    public Operand compileValue(AssemblyFile file) {
        throw new Error("TODO unimplemented");
    }

    public void assign(InterpretValue value) {
        if(value instanceof InterpretObject obj) {
            for(Node child : getChildren()) {
                //TODO move assign to node
                AssignNode.assign(child, obj.get(child.getName()));
            }
        }
        else {
            throwError("Cannot assign " + value + " to " + this);
        }
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
