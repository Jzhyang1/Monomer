package systems.monomer.interpreter.values;

import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.plural.TupleType;

import java.util.List;

public class InterpretTuple extends TupleType implements InterpretValue {
    public static final InterpretTuple EMPTY = new InterpretTuple(List.of());
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
    
    public InterpretValue get(int index) {
        return (InterpretValue) (getTypes().get(index));
    }

    @Override
    public String valueString() {
        return "(" + super.valueString() + ")";
    }

    public InterpretTuple clone() {
//        InterpretTuple ret = (InterpretTuple) super.clone();
//        ret.addAll(getValues().stream().map(e->e.clone()).toList());
//        return ret;
        throw new Error("TODO unimplemented");
    }
}
