package systems.monomer.types.function;


import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.Type;
import systems.monomer.types.pseudo.UnionType;
import systems.monomer.types.signature.Signature;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static systems.monomer.errorhandling.ErrorBlock.programError;

//@Warn non-simplified type
//use OverloadsType after simplification
public class OverloadableType implements Type {
    private TreeMap<Signature, Integer> options = new TreeMap<>();

    public OverloadableType() {
    }

    public OverloadableType(List<? extends Signature> options) {
        options.forEach(this::add);
    }

    private OverloadableType(TreeMap<Signature, Integer> options) {
        this.options = options;
    }

    public void add(Signature option) {
        if(options.put(option, options.size()) != null) {
            throw programError("Duplicate overload", ErrorBlock.Reason.SYNTAX);
        }
    }

    //TODO in Signature, arguments will be their types (it will be stored as a PlaceholderType but when accessing it, it will be the actual type)
    // but in FunctionBody, arguments will be PlaceholderTypes

    @Override
    public Type simplify() {
        //simplify all component types
        options = options.entrySet().stream().collect(Collectors.toMap(
                e -> e.getKey().simplify(),
                Map.Entry::getValue,
                /*there shouldn't be any duplicates*/(a, b) -> {
                    throw programError("Duplicate overload", ErrorBlock.Reason.SYNTAX);
                },
                TreeMap::new
        ));

        if (options.isEmpty()) {
            throw programError("Invalid type", ErrorBlock.Reason.SYNTAX);
        } else if (options.size() == 1) {
            return options.keySet().iterator().next();
        } else {
            return this;
        }
    }

    @Override
    public boolean typeContains(Type other) {
        //only accepts signatures
        if(!(other instanceof Signature sig)) throw programError("Expected signature, got " + other, ErrorBlock.Reason.SYNTAX);
        return options.lowerKey(sig).typeContains(sig);
    }

    @Override
    public Type testReplace(Map<Type, Type> replacements) {
        Type replacement = replacements.get(this);
        if (replacement != null) return replacement;

        TreeMap<Signature, Integer> replaced = options.entrySet().stream().collect(Collectors.toMap(
                e -> e.getKey().testReplace(replacements),
                Map.Entry::getValue,
                /*there shouldn't be any duplicates*/(a, b) -> {
                    throw programError("Duplicate overload", ErrorBlock.Reason.SYNTAX);
                },
                TreeMap::new
        ));

        return new OverloadableType(replaced);
    }

    @Override
    public int serial() {
        return 11_000 - options.size();
    }

    @Override
    public InterpretValue defaultValue() {
        throw programError("Can not get default value of a collection of overloads", ErrorBlock.Reason.RUNTIME);
    }

    @Override
    public String valueString() {
        return options.keySet().stream().map(Type::valueString).collect(Collectors.joining("|"));
    }
}
