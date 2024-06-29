package systems.monomer.syntaxtree.controls;

import lombok.Getter;
import systems.monomer.syntaxtree.operators.OperatorNode;
import systems.monomer.variables.Locality;
import systems.monomer.variables.VariableKey;

import java.util.HashMap;
import java.util.Map;

public abstract class ControlOperatorNode extends OperatorNode implements Locality {
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

    protected ControlOperatorNode(String name){
        super(name);
    }

    public Usage getUsage() {
        return Usage.LABEL;
    }

    public void matchTypes() {
        super.matchTypes();
        setType(getSecond().getType());
    }
}
