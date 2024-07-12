package systems.monomer.types.signature;

import lombok.Getter;
import lombok.Setter;
import systems.monomer.types.Type;
import systems.monomer.types.pseudo.PseudoType;

import java.util.Map;
import java.util.TreeMap;

@Getter
@Setter
public class Signature extends PseudoType {
    protected Type param, namedParam;
    protected Type ret;

    public Signature(Type param, Type namedParam, Type ret) {
        this.param = param;
        this.namedParam = namedParam;
        this.ret = ret;
    }


    @Override
    public boolean hasDependencies() {
        return ret.hasDependencies();
    }

    @Override
    public Type simplify() {
        param = param.simplify();
        namedParam = namedParam.simplify();
        ret = ret.simplify();
        return this;
    }

    @Override
    public Type returnFor(Type arg, Type namedArg) {
        if (!hasDependencies()) return ret;
        //prepare the arguments to be passed in
        Map<Type, Type> replacements = new TreeMap<>();
        replacements.putAll(param.assign(arg));
        replacements.putAll(namedParam.assign(namedArg));

        //pass in the arguments and evaluate the type
        Type t = ret.testReplace(replacements).simplify();

        //if there are any return types unwrap them
        return t.unwrapReturns();
    }

    @Override
    public Type testReplace(Map<Type, Type> replacements) {
        Type replacement = replacements.get(this);
        if (replacement != null) return replacement;

        return new Signature(param.testReplace(replacements), namedParam.testReplace(replacements), ret.testReplace(replacements));
    }

    @Override
    public boolean typeContains(Type other) {
        return other instanceof Signature s &&
                param.typeContains(s.param) &&
                namedParam.typeContains(s.namedParam);
    }


    @Override
    public int serial() {
        return 1600_000 + param.serial() + namedParam.serial();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Signature s &&
                param.equals(s.param) &&
                namedParam.equals(s.namedParam) &&
                ret.equals(s.namedParam);
    }

    @Override
    public String valueString() {
        return namedParam.valueString() + param.valueString() + "->" + ret.valueString();
    }
}
