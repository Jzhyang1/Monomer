package systems.monomer.interpreter;

import systems.monomer.types.Type;

import java.util.ArrayList;
import java.util.List;

public class InterpretList extends InterpretCollection {
    public static InterpretList EMPTY = new InterpretList(ANY);
    private final List<InterpretValue> values = new ArrayList<>();

    public InterpretList(Type elementType){
        super(elementType);
    }
    public InterpretList(List<InterpretValue> list) {
        //todo set the type to the most general type
        super(list.get(0));
        values.addAll(list);
    }

    public List<InterpretValue> getValues() {
        return values;
    }

    public void add(InterpretValue value) {
        values.add(value);
    }

    @Override
    public String valueString() {
        return "[" + super.valueString() + "]";
    }

    public InterpretList clone() {
        throw new Error("TODO unimplemented");
    }
}
