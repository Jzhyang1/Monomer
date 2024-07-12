package systems.monomer.types.function;

import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.Type;
import systems.monomer.types.signature.Signature;

import static systems.monomer.util.Util.lowerBound;

import java.util.ArrayList;
import java.util.List;

public class OverloadsType implements Type {
    private final List<Type> options = new ArrayList<>();

    public OverloadsType() {
    }

    public OverloadsType(List<? extends Type> options) {
        this.options.addAll(options);
        options.sort(null);
    }

    public int randomAccessIndex(Signature signature) {
        return lowerBound(options, signature);
    }

    //TODO
    @Override
    public InterpretValue defaultValue() {
        return null;
    }

    @Override
    public boolean typeContains(Type other) {
        return false;
    }

    @Override
    public int serial() {
        return 0;
    }

    @Override
    public String valueString() {
        return "";
    }
}
