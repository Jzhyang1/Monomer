package systems.monomer.interpreter;

import systems.monomer.interpreter.values.InterpretTuple;
import systems.monomer.interpreter.variables.InterpretKey;
import systems.monomer.interpreter.variables.InterpretVariable;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.variables.VariableKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InterpretModuleNode extends ModuleNode implements InterpretNode, InterpretLocality {

    public InterpretModuleNode(String name) {
        super(name);
    }

    @Override
    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot interpret a module as a variable");
    }

    public InterpretResult interpretValue() {
        initVariables();

        List<InterpretValue> ret = new ArrayList<>();
        for(InterpretNode child : getChildrenInterpretNodes()) {
            InterpretResult result = child.interpretValue();

            if (!result.isValue()) return result;
            ret.add(result.asValue());
        }
        return new InterpretTuple(ret);
    }

    public void setVariableValues(Map<String, VariableKey> values) {
        for(Map.Entry<String, VariableKey> entry : values.entrySet()) {
            InterpretKey original = (InterpretKey) getVariables().get(entry.getKey());
            InterpretKey modified = (InterpretKey) entry.getValue();
            original.setValue(modified.getValue());
        }
    }
}
