package systems.monomer.interpreter.values;

import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.Type;
import systems.monomer.types.collection.SequenceType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static systems.monomer.errorhandling.ErrorBlock.programError;

//TODO make this a SequenceType
public final class InterpretSequence extends SequenceType implements InterpretCollection {
    private final List<InterpretValue> values = new ArrayList<>();

    public InterpretSequence(Type elementType) {
        super(elementType);
    }

    public InterpretSequence(Collection<? extends InterpretValue> list) {
        //TODO set the type to the most general type
        super(list.iterator().next());
        values.addAll(list);
    }

    public Collection<InterpretValue> getValues() {
        return values;
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
        return new InterpretSequence(getElementType());
    }

    public InterpretSequence clone() {
        InterpretSequence ret = new InterpretSequence(this.getElementType());
        ret.addAll(getValues());
        return ret;
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
}
