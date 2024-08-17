package systems.monomer.interpreter.values;

import lombok.Getter;
import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.collection.StringType;
import systems.monomer.util.ValueStringListEquivalent;

import java.util.Collection;

import static systems.monomer.errorhandling.ErrorBlock.programError;

@Getter
public class InterpretString extends StringType implements InterpretCollection {
    private final ValueStringListEquivalent value;
    public InterpretString(String value) {
        this.value = new ValueStringListEquivalent(value);
    }

    @Override
    public Collection<? extends InterpretValue> getValues() {
        return value;
    }

    @Override
    public InterpretValue getSingleValueAt(InterpretValue index) {
        if(!(index instanceof InterpretInt i)) throw programError("Index is not an integer", ErrorBlock.Reason.RUNTIME);
        return value.get(i.getValue());
    }

    @Override
    public void setSingleValueAt(InterpretValue index, InterpretValue value) {
        if (!(index instanceof InterpretInt i)) throw programError("Index is not an integer", ErrorBlock.Reason.RUNTIME);
        if(!(value instanceof InterpretChar cvalue)) throw programError("Value is not a character", ErrorBlock.Reason.RUNTIME);
        this.value.set(i.getValue(), cvalue);
    }

    @Override
    public void add(InterpretValue v) {
        value.add(v.getValue());
    }

    @Override
    public void addAll(Collection<? extends InterpretValue> values) {
        value.addAll((Collection<InterpretChar>) values);
    }

    @Override
    public InterpretCollection emptyCopy() {
        return new InterpretString("");
    }

    public String valueString() {
        return value.toString();
    }

    public ValueStringListEquivalent getValue() {
        return value;
    }

    @Override
    public int compareValueTo(InterpretValue other) {
        if(!(other instanceof InterpretString otherString)) {
            return compareTo(other);
        }

        return value.compareTo(otherString.value);
    }

    @Override
    public InterpretString clone() {
        try {
            return (InterpretString) super.clone();
        } catch (CloneNotSupportedException e) {
            throw programError(e.getMessage(), ErrorBlock.Reason.OTHER);
        }
    }
}
