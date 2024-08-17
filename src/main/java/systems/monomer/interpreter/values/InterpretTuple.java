package systems.monomer.interpreter.values;

import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.Type;
import systems.monomer.types.tuple.TupleType;

import java.util.Iterator;
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

    public InterpretTuple(List<InterpretValue> list) {
        super(list);
    }
    
    public InterpretValue get(int index) {
        return (InterpretValue) (super.get(index));
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

    @Override
    public int compareValueTo(InterpretValue other) {
        if(!(other instanceof InterpretTuple otherTuple)) return compareTo(other);

        Iterator<Type> thisIterator = this.getChildren().iterator();
        Iterator<Type> otherIterator = otherTuple.getChildren().iterator();

        while(thisIterator.hasNext() && otherIterator.hasNext()) {
            InterpretValue thisValue = (InterpretValue) thisIterator.next();
            InterpretValue otherValue = (InterpretValue) otherIterator.next();

            int comparison = thisValue.compareValueTo(otherValue);
            if(comparison != 0) return comparison;
        }

        if(thisIterator.hasNext()) return 1;
        if(otherIterator.hasNext()) return -1;
        return 0;
    }
}
