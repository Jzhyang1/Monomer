package systems.monomer.types.function;

import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.values.InterpretOverloads;
import systems.monomer.types.Type;
import systems.monomer.types.pseudo.UnionType;
import systems.monomer.types.signature.Signature;

import static systems.monomer.util.Util.lowerBound;

import java.util.ArrayList;
import java.util.List;

public class OverloadsType implements Type {
    protected final List<Signature> options = new ArrayList<>();

    public OverloadsType(int size) {
        for(int i = 0; i < size; ++i) options.add(null);
    }

    public OverloadsType(List<Signature> options) {
        this.options.addAll(options);
        options.sort(null);
    }

    public Type returnsFor(Signature callSig) {
        UnionType<Type> ret = new UnionType<>();
        for(int i = lowerBound(options, callSig); callSig.typeContains(options.get(i)); ++i) {
            ret.add(options.get(i).getRet());
        }
        return ret.simplify();
    }

    public int randomAccessIndex(Signature signature) {
        return lowerBound(options, signature);
    }

    public Signature get(int i) {
        return options.get(i);
    }

    @Override
    public InterpretValue defaultValue() {
        return new InterpretOverloads(options.size());
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
