package systems.monomer.interpreter;

import systems.monomer.syntaxtree.Node;
import systems.monomer.types.TupleType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InterpretTuple extends TupleType implements InterpretValue {
    public static InterpretTuple EMPTY = new InterpretTuple(List.of());
    public static InterpretTuple toTuple(Collection<? extends Node> list) {
        return new InterpretTuple(list.stream().map(Node::interpretValue).toList());
    }
    public static InterpretTuple toTuple(InterpretValue value) {
        if (value instanceof InterpretTuple) {
            return (InterpretTuple) value;
        } else {
            return new InterpretTuple(List.of(value));
        }
    }

    public InterpretTuple(List<? extends InterpretValue> list) {
        addAll(list);
    }

    public List<InterpretValue> getValues() {
        return (List<InterpretValue>) (List) getTypes();    //TODO fix this
    }
    
    public InterpretValue get(int index) {
        return getValues().get(index);
    }

    @Override
    public String valueString() {
        return "(" + super.valueString() + ")";
    }

    public InterpretTuple clone() {
        throw new Error("TODO unimplemented");
    }
}
