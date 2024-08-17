package systems.monomer.interpreter.values;

import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.function.OverloadsType;
import systems.monomer.types.signature.Signature;
import systems.monomer.variables.FunctionBody;

import java.util.List;

import static systems.monomer.errorhandling.ErrorBlock.programError;

public class InterpretOverloads extends OverloadsType implements InterpretValue {
    public InterpretOverloads(int size) {
        super(size);
    }

    public void setOverload(int randomAccessIndex, FunctionBody body) {
        options.set(randomAccessIndex, body);
    }
    public FunctionBody getOverload(int randomAccessIndex) {
        return (FunctionBody) options.get(randomAccessIndex);
    }

    @Override
    public InterpretValue clone() {
        try {
            return (InterpretValue) super.clone();
        } catch (CloneNotSupportedException e) {
            throw programError(e.getMessage(), ErrorBlock.Reason.OTHER);
        }
    }

    @Override
    public int compareValueTo(InterpretValue other) {
        //it doesn't make sense to compare sets of overloads so an arbitrary comparison is made
        if (!(other instanceof InterpretOverloads otherOverloads)) {
            return compareTo(other);
        }

        return options.size() - otherOverloads.options.size();
    }
}
