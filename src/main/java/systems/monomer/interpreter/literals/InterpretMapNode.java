package systems.monomer.interpreter.literals;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.interpreter.values.InterpretList;
import systems.monomer.syntaxtree.literals.MapNode;

import java.util.ArrayList;
import java.util.List;

public class InterpretMapNode extends MapNode implements InterpretNode {

    @Override
    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot interpret map value as variable");
    }

    public InterpretResult interpretValue() {
        //TODO change into map
        List<InterpretValue> ret = new ArrayList<>();
        for(InterpretNode child : getChildrenInterpretNodes()) {
            InterpretResult result = child.interpretValue();
            if (!result.isValue()) return result;

            ret.add(result.asValue());
        }
        return new InterpretList(ret);
    }
}
