package systems.monomer.interpreter.literals;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.variables.InterpretVariable;
import systems.monomer.interpreter.values.InterpretCollection;
import systems.monomer.interpreter.values.InterpretList;
import systems.monomer.syntaxtree.literals.ListNode;
import systems.monomer.types.collection.SequenceType;

import java.util.ArrayList;
import java.util.List;

public class InterpretListNode extends ListNode implements InterpretNode {
    @Override
    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot interpret list value as variable");
    }

    public InterpretResult interpretValue() {
        List<InterpretValue> ret = new ArrayList<>();
        for(InterpretNode child : getChildrenInterpretNodes()) {
            InterpretResult result = child.interpretValue();
            if (!result.isValue()) return result;

            //TODO handle this somewhere else
            InterpretValue value = result.asValue();
            if(SequenceType.SEQUENCE.typeContains(value))
                ret.addAll(((InterpretCollection)value).getValues());
            else
                ret.add(value);
        }
        return new InterpretList(ret);
    }

}
