package systems.monomer.interpreter.values;

import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.Type;
import systems.monomer.types.collection.ListType;
import systems.monomer.types.pseudo.AnyType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static systems.monomer.errorhandling.ErrorBlock.programError;

public class InterpretList extends ListType implements InterpretCollection {
    public static InterpretList EMPTY = new InterpretList(AnyType.ANY);
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

    @Override
    public InterpretValue getSingleValueAt(InterpretValue index) {
        if(!(index instanceof InterpretInt i)) throw programError("Index is not an integer", ErrorBlock.Reason.RUNTIME);
        return values.get(i.getValue());
    }

    @Override
    public void setSingleValueAt(InterpretValue index, InterpretValue value) {
        if (!(index instanceof InterpretInt i)) throw programError("Index is not an integer", ErrorBlock.Reason.RUNTIME);
        values.set(i.getValue(), value);
    }

    public void add(InterpretValue value) {
        values.add(value);
    }

    @Override
    public void addAll(Collection<? extends InterpretValue> values) {
        this.values.addAll(values);
    }

    @Override
    public InterpretCollection emptyCopy() {
        return new InterpretList(getElementType());
    }

    @Override
    public String valueString() {
        return "[" + super.valueString() + "]";
    }

    public InterpretList clone() {
        throw new Error("TODO unimplemented");
    }
}
