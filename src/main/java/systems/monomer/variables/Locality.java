package systems.monomer.variables;

import org.jetbrains.annotations.Nullable;
import systems.monomer.syntaxtree.Node;

import java.util.Map;

public interface Locality {
    Map<String, VariableKey> getVariables();
    Node getParent();

    default void putLocalizedVariable(String varName, VariableKey key) {
        getVariables().put(varName, key);
    }
    default @Nullable VariableKey getLocalizedVariable(String varName) {
        VariableKey ret = getVariables().get(varName);
        if(ret != null) return ret;
        return getParent() == null ? null : getParent().getVariable(varName);
    }

    default void initVariables() {
        for(VariableKey key : getVariables().values()) {
            key.setValue(key.getType().defaultValue());
        }
    }
}
