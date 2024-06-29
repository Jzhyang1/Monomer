package systems.monomer.interpreter;

import systems.monomer.interpreter.values.InterpretTuple;
import systems.monomer.syntaxtree.ModuleNode;

import java.util.ArrayList;
import java.util.List;

public class InterpretModuleNode extends ModuleNode implements InterpretNode {

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
}
