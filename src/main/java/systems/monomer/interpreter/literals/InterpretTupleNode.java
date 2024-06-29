package systems.monomer.interpreter.literals;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.interpreter.values.InterpretTuple;
import systems.monomer.syntaxtree.literals.TupleNode;

import java.util.ArrayList;
import java.util.List;

public class InterpretTupleNode extends TupleNode implements InterpretNode {
    public InterpretTupleNode(){}
    public InterpretTupleNode(String name){
        super(name);
    }

    @Override
    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot interpret tuple value as variable");
    }

    public InterpretResult interpretValue() {
        List<InterpretValue> ret = new ArrayList<>();
        for (InterpretNode child : getChildrenInterpretNodes()) {
            InterpretResult result = child.interpretValue();
            if (!result.isValue()) return result;

            ret.add(result.asValue());
        }
        return new InterpretTuple(ret);
    }

    @Override
    public boolean interpretAssign(InterpretValue value, boolean toLock) {
        if (!InterpretTuple.EMPTY.typeContains(value))
            return InterpretNode.super.interpretAssign(value, toLock);

        InterpretTuple tuple = (InterpretTuple) value;
        for (int i = 0; i < tuple.size(); i++) {
            getInterpretNode(i).interpretAssign(tuple.get(i), toLock);
        }
        return true;
    }

}
