package systems.monomer.syntaxtree.literals;

import lombok.Getter;
import systems.monomer.syntaxtree.Node;
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
}
