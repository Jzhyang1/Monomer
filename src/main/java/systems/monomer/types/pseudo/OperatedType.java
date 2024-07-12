package systems.monomer.types.pseudo;

import systems.monomer.types.Type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OperatedType extends PseudoType {
    //honestly this could be linked list
    private final List<Type> opTypes = new ArrayList<>();
    private final Function<List<Type>, Type> conversion;

    public OperatedType(Function<List<Type>, Type> conversion) {
        this.conversion = conversion;
    }

    public OperatedType(Type a, Type b, Function<List<Type>, Type> conversion) {
        opTypes.add(a);
        opTypes.add(b);
        this.conversion = conversion;
    }

    public OperatedType(Collection<Type> types, Function<List<Type>, Type> conversion) {
        opTypes.addAll(types);
        this.conversion = conversion;
    }


    @Override
    public boolean hasDependencies() {
        return conversion.apply(opTypes).hasDependencies();
    }

    @Override
    public Type simplify() {
        opTypes.replaceAll(Type::simplify);
        List<Type> newUnionType = new ArrayList<>();

        boolean isUnion = false;

        //returns if a breaking type occurs
        //creates a list of all possible combinations of operand types if there are any UnionTypes
        for (Type t : opTypes) {
            if (t == ErasedType.ERASED) return ErasedType.ERASED;
            if (t instanceof ReturnType) return t;


            //handle union expansion
            if (t instanceof UnionType ut) {
                isUnion = true;

                if (newUnionType.isEmpty()) newUnionType.addAll(ut.getOptions());
                else {
                    List<Type> unionExpansion = new ArrayList<>();
                    for (Type u : newUnionType) {
                        for (Type v : ut.getOptions()) {
                            unionExpansion.add(new OperatedType(u, v, conversion));
                        }
                    }
                    newUnionType = unionExpansion;
                }
            } else {
                if (newUnionType.isEmpty()) newUnionType.add(t);
                else {
                    List<Type> unionExpansion = new ArrayList<>();
                    for (Type u : newUnionType) {
                        unionExpansion.add(new OperatedType(u, t, conversion));
                    }
                    newUnionType = unionExpansion;
                }
            }
        }

        //not necessarily a union
        return isUnion ? (new UnionType(newUnionType)).simplify() : conversion.apply(opTypes);
    }

    @Override
    public Type testReplace(Map<Type, Type> replacements) {
        Type replacement = replacements.get(this);
        if (replacement != null) return replacement;

        return new OperatedType(
                opTypes.stream().map(a -> a.testReplace(replacements)).collect(Collectors.toList()),
                conversion);
    }

    @Override
    public String valueString() {
        return opTypes.stream().map(Type::valueString).collect(Collectors.joining("*"));
    }
}
