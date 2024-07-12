package systems.monomer.types.pseudo;


import systems.monomer.types.Type;
import systems.monomer.types.tuple.TupleType;

import java.util.Map;


public class ReturnType extends PseudoType {
    public static ReturnType VOID = new ReturnType();

    private final Type returns;

    public ReturnType() {
        returns = TupleType.EMPTY;
    }

    public ReturnType(Type ret) {
        returns = ret;
    }

    @Override
    public boolean hasDependencies() {
        return returns.hasDependencies();
    }

    @Override
    public Type simplify() {
        returns.simplify();
        return this;
    }

    @Override
    public Type testReplace(Map<Type, Type> replacements) {
        Type replacement = replacements.get(this);
        if (replacement != null) return replacement;

        return new ReturnType(returns.testReplace(replacements));
    }

    @Override
    public Type unwrapReturns() {
        return returns.unwrapReturns();
    }

    @Override
    public int compareTo(Type o) {
        int diff = super.compareTo(o);
        if (diff != 0) return diff;

        ReturnType rt = (ReturnType) o;
        return returns.compareTo(rt.returns);
    }

    @Override
    public int serial() {
        return super.serial() + (returns != null ? returns.serial() : 0);
    }

    @Override
    public String valueString() {
        return "RETURN(" + returns.valueString() + ")";
    }
}
