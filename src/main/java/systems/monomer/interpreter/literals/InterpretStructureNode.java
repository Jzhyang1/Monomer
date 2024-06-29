package systems.monomer.interpreter.literals;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.interpreter.values.InterpretObject;
import systems.monomer.syntaxtree.literals.StructureNode;
import systems.monomer.variables.VariableKey;

import java.util.Map;

public class InterpretStructureNode extends StructureNode implements InterpretNode {
    @Override
    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot assign to structure " + this);
    }

    public InterpretResult interpretValue() {
        initVariables();

        //evaluate body
        for(InterpretNode child : getChildrenInterpretNodes()) {
            InterpretResult res = child.interpretValue();
            if(!res.isValue())
                return res;
        }

        //return object
        InterpretObject ret = new InterpretObject();
        for(Map.Entry<String, VariableKey> entry : getVariables().entrySet()) {
            //TODO copy over variable entries instead of values only
            ret.set(entry.getKey(), entry.getValue().getValue());
        }
        return ret;
    }

    public boolean interpretAssign(InterpretValue value, boolean toLock) {
        if(value instanceof InterpretObject obj) {
            for(Map.Entry<String, VariableKey> entry : getVariables().entrySet()) {
                if(entry.getValue().isLocked()) throw syntaxError("Cannot assign to constant " + entry.getKey());
                if(obj.hasField(entry.getKey())) entry.getValue().setValue(obj.get(entry.getKey()));
                if(toLock) entry.getValue().lock();
            }
            return true;
        }
        return false;
    }
}
