package systems.monomer.interpreter;

import systems.monomer.syntaxtree.Node;
import systems.monomer.types.Type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InterpretList extends InterpretCollection {
    public static InterpretList EMPTY = new InterpretList(ANY);
    private List<InterpretValue> values = new ArrayList<>();

    public InterpretList(Type elementType){
        super(elementType);
    }
    public InterpretList(List<InterpretValue> list) {
        //todo set the type to the most general type
        super(list.get(0));
        values = list;
    }

    public List<InterpretValue> getValues() {
        return values;
    }
    @Override
    public String valueString() {
        return "[" + super.valueString() + "]";
    }

    public InterpretList clone() {
        throw new Error("TODO unimplemented");
    }
}
