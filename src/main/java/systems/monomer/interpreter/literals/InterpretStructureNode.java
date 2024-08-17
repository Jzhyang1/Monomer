package systems.monomer.interpreter.literals;

import systems.monomer.interpreter.InterpretLocality;
import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.variables.InterpretKey;
import systems.monomer.interpreter.variables.InterpretVariable;
import systems.monomer.interpreter.values.InterpretObject;
import systems.monomer.syntaxtree.literals.StructureNode;
import systems.monomer.variables.VariableKey;

import java.util.Map;

public class InterpretStructureNode extends StructureNode implements InterpretNode, InterpretLocality {
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
            InterpretKey variable = (InterpretKey) entry.getValue();
            ret.set(entry.getKey(), variable.getValue());
        }
        return ret;
    }

    public boolean interpretAssign(InterpretValue value, boolean toLock) {
        if(value instanceof InterpretObject obj) {
            for(Map.Entry<String, VariableKey> entry : getVariables().entrySet()) {
                if(entry.getValue().isLocked()) throw syntaxError("Cannot assign to constant " + entry.getKey());
                if(obj.hasField(entry.getKey())) {
                    InterpretKey variable = (InterpretKey) entry.getValue();
                    variable.setValue(obj.getField(entry.getKey()));
                }
                if(toLock) entry.getValue().lock();
            }
            return true;
        }
        return false;
    }
}
