package systems.monomer.types.tuple;


import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.values.InterpretTuple;
import systems.monomer.types.Type;
import systems.monomer.util.Util;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class TupleType implements Type {
    public static final TupleType EMPTY = new TupleType();

    public static TupleType asTuple(Type type) {
        if (type instanceof TupleType tuple) return tuple;
        return new TupleType(List.of(type));
    }


    protected final List<Type> sequence = new LinkedList<>();

    public TupleType() {
    }

    public TupleType(List<Type> sequence) {
        this.sequence.addAll(sequence);
    }

    @Override
    public List<Type> getChildren() {
        return sequence;
    }

//    @Override
//    public Type replace(Type t, Type newT) {
//        if (this.equals(t)) return newT;
//
//        sequence.replaceAll(a -> a.replace(t, newT));
//        return this;
//    }


    @Override
    public boolean hasDependencies() {
        return sequence.stream().anyMatch(Type::hasDependencies);
    }

    protected TupleType simplifySequence() {
        //flatten all FlatTuples
        //similar to UnionType.simplifyUnion
        for (ListIterator<Type> iter = sequence.listIterator(); iter.hasNext(); ) {
            if (iter.next() instanceof FlatTupleType ftt) {
                iter.remove();

                for (Type t : ftt.simplifySequence().sequence) iter.add(t);
            }
        }
        return this;
    }

    @Override
    public Type simplify() {
        simplifySequence();
        sequence.replaceAll(Type::simplify);
        return this;
    }

    @Override
    public Map<Type, Type> assign(Type newT) {
        //can not use typeContains here because of PlaceholderTypes
        if (!(newT instanceof TupleType tt) || tt.sequence.size() > sequence.size())
            throw new RuntimeException("Can not assign " + newT.valueString() + " to " + valueString());

        Map<Type, Type> ret = new TreeMap<>();

        Util.pairCheck(
                sequence, tt.sequence,
                (a, b) -> {
                    ret.putAll(a.assign(b));
                    return false;
                }, false);

        return ret;
    }

    @Override
    public Type testReplace(Map<Type, Type> replacements) {
        Type replacement = replacements.get(this);
        if (replacement != null) return replacement;

        try {
            return this.getClass().getDeclaredConstructor(List.class).newInstance(sequence.stream().map(a -> a.testReplace(replacements)).collect(Collectors.toList()));
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException("Internal error in " + this.getClass().getName());
        }
    }

    @Override
    public InterpretValue defaultValue() {
        return new InterpretTuple(sequence.stream()
                .map(Type::defaultValue)
                .toList());
    }

    @Override
    public boolean typeContains(Type other) {
        if (!(other instanceof TupleType tt)) return false;

        simplifySequence();
        if (sequence.size() < tt.sequence.size()) return false;

        Iterator<Type> compIter = tt.sequence.iterator();
        Iterator<Type> refIter = sequence.iterator();

        while (compIter.hasNext()) {
            if (!refIter.next().typeContains(compIter.next())) return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof TupleType tt && sequence.equals(tt.sequence);
    }


    @Override
    public int serial() {
        return 210_000 - sequence.size();
    }

    @Override
    public int compareTo(Type o) {
        int c = Type.super.compareTo(o);
        if (c != 0) return c;

        //other has to be TupleType now
        TupleType tt = (TupleType) o;

        //compare element-by-element
        return Util.pairCheck(sequence, tt.sequence, Type::compareTo, 0);
    }

    @Override
    public String valueString() {
        return "(" + sequence.stream().map(Type::valueString).collect(Collectors.joining(",")) + ")";
    }
}
