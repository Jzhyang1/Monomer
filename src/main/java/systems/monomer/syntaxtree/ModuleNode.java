package systems.monomer.syntaxtree;

import lombok.Getter;
import systems.monomer.variables.Locality;
import systems.monomer.variables.VariableKey;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ModuleNode extends Node implements Locality {
    public ModuleNode(String name) {
        super(name);
    }

    public Usage getUsage() {
        return Usage.MODULE;
    }

    @Getter
    private final Map<String, VariableKey> variables = new HashMap<>();
    @Override
    public VariableKey getVariable(String varName) {
        return getLocalizedVariable(varName);
    }
    @Override
    public void putVariable(String varName, VariableKey key) {
        putLocalizedVariable(varName, key);
    }

    public Map<String, VariableKey> getVariableValuesMap() {
        //TODO optimize based on whether constant
        return variables.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().clone(),
                        (a, b) -> b,
                        HashMap::new
                ));
    }
    public void setVariableValues(Map<String, VariableKey> values) {
        for(Map.Entry<String, VariableKey> entry : values.entrySet()) {
            VariableKey original = variables.get(entry.getKey());
            original.setValue(entry.getValue().getValue());
        }
    }
}
