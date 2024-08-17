package systems.monomer.interpreter.values;

import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.collection.MapType;
import systems.monomer.types.Type;
import systems.monomer.types.tuple.TupleType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static systems.monomer.errorhandling.ErrorBlock.programError;

public class InterpretMap extends MapType implements InterpretCollection {
    private final Map<Object, InterpretValue> map = new HashMap<>();

    public InterpretMap(Type elementType, Type valueType) {
        super(elementType, valueType);
    }
    public InterpretMap(TupleType pair) {
        super(pair);
    }

    @Override
    public Collection<? extends InterpretValue> getValues() {
        //TODO how will this be used?
        return map.values();
    }

    @Override
    public InterpretValue getSingleValueAt(InterpretValue index) {
        return map.get(index.getValue());
    }

    @Override
    public void setSingleValueAt(InterpretValue index, InterpretValue value) {
        map.put(index.getValue(), value);
    }

    @Override
    public void add(InterpretValue value) {
        map.put(value.getValue(), value);
    }

    @Override
    public void addAll(Collection<? extends InterpretValue> values) {
        for(InterpretValue value : values) {
            add(value);
        }
    }

    @Override
    public InterpretCollection emptyCopy() {
        return new InterpretMap((TupleType) getElementType());
    }

    public String valueString() {
        return map.toString();
    }

    public InterpretMap clone() {
        try {
            InterpretMap ret = (InterpretMap) super.clone();
            ret.map.replaceAll((k, v) -> v.clone());
            return ret;
        } catch (CloneNotSupportedException e) {
            throw programError("Unable to clone " + this.valueString(), ErrorBlock.Reason.OTHER);
        }
    }
}
