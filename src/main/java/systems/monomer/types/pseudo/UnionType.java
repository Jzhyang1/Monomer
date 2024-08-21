package systems.monomer.types.pseudo;

import lombok.Getter;
import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.types.Type;
import systems.monomer.util.Util;

import java.util.*;
import java.util.stream.Collectors;

import static systems.monomer.errorhandling.ErrorBlock.programError;

/**
 * UnionType is used primarily to temporarily hold PlaceholderTypes
 */
@Getter
public class UnionType<T extends Type> extends PseudoType {
    //TODO cache whether options is simplified
    private final List<T> options = new LinkedList<>();
    private boolean isSimplified = false;

    public UnionType() {
    }

    public UnionType(Collection<? extends T> options) {
        this.options.addAll(options);
    }

    /**
     * adds an option to the UnionType
     */
    public void add(T option) {
        options.add(option);
        isSimplified = false;
    }

    /**
     * flattens all union types and removes duplicates
     *
     * @return this
     */
    protected UnionType<T> simplifyOptions() {
        if(isSimplified) return this;

        //flatten all internal UnionTypes
        //similar to FlatTupleType.simplifyTuple
        for (ListIterator<T> iter = options.listIterator(); iter.hasNext(); ) {
            if (iter.next() instanceof UnionType ut) {
                iter.remove();
                List<T> tList = ut.simplifyOptions().options;

                for (T t : tList) iter.add(t);
            }
        }

        //keep only unique values
        //remove any ErasedTypes
        options.sort(null);    //most inclusive types to the left

        ListIterator<T> uniqueIter = options.listIterator();  //iterator pointing to position after the last unique value
        T lastUnique = uniqueIter.next();   //the value of the last unique value

        for (T t : options.subList(1, options.size())) {
            //use typeContains to keep the most inclusive types
            if (!lastUnique.typeContains(t) && t != ErasedType.ERASED) {
                uniqueIter.next();  //point to next available space
                uniqueIter.set(lastUnique = t);  //store the unique value
            }
        }
        options.subList(uniqueIter.nextIndex(), options.size()).clear();

        isSimplified = true;
        return this;
    }

    @Override
    public boolean hasDependencies() {
        return options.stream().anyMatch(Type::hasDependencies);
    }

    @Override
    public Type simplify() {
        //simplify all component types
        options.replaceAll(e->(T)e.simplify());

        simplifyOptions();

        if (options.isEmpty()) {
            throw programError("Invalid type", ErrorBlock.Reason.SYNTAX);
        } else if (options.size() == 1) {
            return options.get(0);
        } else {
            return this;
        }
    }

    @Override
    public Type testReplace(Map<Type, Type> replacements) {
        Type replacement = replacements.get(this);
        if (replacement != null) return replacement;

        return new UnionType(options.stream().map(a -> a.testReplace(replacements)).collect(Collectors.toList()));
    }

    @Override
    public Type unwrapReturns() {
        options.replaceAll(e->(T)e.unwrapReturns());
        isSimplified = false;
        return this;
    }

    @Override
    public boolean typeContains(Type other) {
        if (!(other instanceof UnionType ut)) return options.stream().anyMatch(a -> a.typeContains(other));

        simplifyOptions();
        ut.simplifyOptions();

        //2 pointers
        Iterator<T> refIter = options.iterator();
        Iterator<T> compIter = ut.options.iterator();

        while (compIter.hasNext()) {
            if (!refIter.hasNext()) return false;

            Type ref = refIter.next();
            Type comp = compIter.next();

            while (!ref.typeContains(comp) && refIter.hasNext()) ref = refIter.next();
            if (!ref.typeContains(comp)) return false;

            while (ref.typeContains(comp) && compIter.hasNext()) comp = compIter.next();
        }

        return true;
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof UnionType ut)) return false;

        simplifyOptions();
        ut.simplifyOptions();

        return (serial() == ut.serial()) &&
                options.equals(ut.options);
    }

    @Override
    public int serial() {
        return 10_000 - options.size();
    }

    @Override
    public int compareTo(Type o) {
        int c = super.compareTo(o);
        if (c != 0) return c;

        //the other has to be UnionType now
        //compare element-by-element
        UnionType ut = (UnionType) o;

        simplifyOptions();
        ut.simplifyOptions();

        //compare element-by-element
        return Util.pairCheck(options, ut.options, Type::compareTo, 0);
    }

    @Override
    public String valueString() {
        return options.stream().map(Type::valueString).collect(Collectors.joining("|"));
    }
}
