package systems.monomer.syntaxtree.literals;

import lombok.Getter;
import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretObject;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.operators.AssignNode;
import systems.monomer.types.ObjectType;
import systems.monomer.variables.Locality;
import systems.monomer.variables.VariableKey;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructureNode extends LiteralNode implements Locality {
    public static final StructureNode EMPTY = new StructureNode();

    @Getter
    private final Map<String, VariableKey> variables = new HashMap<>();

    public StructureNode(){}
    public StructureNode(List<Node> children) {
        children.forEach(this::add);
    }

    public static boolean isStructure(Node dest) {
        return dest instanceof StructureNode;   //TODO
    }

    public Collection<String> getFieldNames() {
        return variables.keySet();
    }

    @Override
    public VariableKey getVariable(String varName) {
        return getLocalizedVariable(varName);
    }
    @Override
    public void putVariable(String varName, VariableKey key) {
        putLocalizedVariable(varName, key);
    }

    public void matchTypes() {
        super.matchTypes();
        ObjectType ret = new ObjectType();
        for(Map.Entry<String, VariableKey> entry : variables.entrySet()) {
            ret.setField(entry.getKey(), entry.getValue());
        }
        setType(ret);
    }

    public InterpretResult interpretValue() {
        initVariables();

        //evaluate body
        for(Node child : getChildren()) {
            InterpretResult res = child.interpretValue();
            if(!res.isValue())
                return res;
        }

        //return object
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

    public void interpretAssign(InterpretValue value, boolean toLock) {
        if(value instanceof InterpretObject obj) {
            for(Map.Entry<String, VariableKey> entry : variables.entrySet()) {
                if(entry.getValue().isLocked()) throw syntaxError("Cannot assign to constant " + entry.getKey());
                if(obj.hasField(entry.getKey())) entry.getValue().setValue(obj.get(entry.getKey()));
                if(toLock) entry.getValue().lock();
            }
        }
        else {
            throw syntaxError("Cannot assign " + value + " to " + this);
        }
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
