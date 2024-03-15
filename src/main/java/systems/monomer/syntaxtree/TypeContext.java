package systems.monomer.syntaxtree;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import systems.monomer.types.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * Used in function calls to resolve incomplete types
 * look for usage in CastToFunctionNode
 */
public class TypeContext {
    @Getter
    private final Map<String, Type> variables = new HashMap<>();
    private final Node parent;

    @Getter @Setter
    private @Nullable Type returnType;

    public TypeContext(Node parent) {
        this.parent = parent;
    }

    public void putVariableType(String varName, Type type) {
        variables.put(varName, type);
    }
    public Type getVariableType(String varName) {
        Type ret = variables.get(varName);
        if(ret == null) return parent.getVariable(varName).getType();
        return ret;
    }
}
