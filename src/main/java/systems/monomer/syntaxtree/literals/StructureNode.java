package systems.monomer.syntaxtree.literals;

import lombok.Getter;
import systems.monomer.types.object.ObjectType;
import systems.monomer.variables.Locality;
import systems.monomer.variables.VariableKey;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
public class StructureNode extends LiteralNode implements Locality {
    private final Map<String, VariableKey> variables = new HashMap<>();

    public StructureNode(){}

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
            ret.setField(entry.getKey(), entry.getValue().getType());
        }
        setType(ret);
    }
}
