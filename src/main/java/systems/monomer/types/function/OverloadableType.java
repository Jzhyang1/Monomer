package systems.monomer.types.function;


import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.types.Type;
import systems.monomer.types.pseudo.UnionType;
import systems.monomer.types.signature.Signature;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static systems.monomer.errorhandling.ErrorBlock.programError;

//@Warn non-simplified type
//use OverloadsType after simplification
public class OverloadableType extends UnionType<Signature> {
    public OverloadableType() {
    }

    public OverloadableType(List<? extends Signature> options) {
        super(options);
    }



    @Override
    protected OverloadableType simplifyOptions() {
        getOptions().sort(null);    //most inclusive types to the left
        return this;
    }

    @Override
    public Type simplify() {
        //simplify all component types
        getOptions().replaceAll(Signature::simplify);

        if (getOptions().isEmpty()) {
            throw programError("Invalid type", ErrorBlock.Reason.SYNTAX);
        } else if (getOptions().size() == 1) {
            return getOptions().get(0);
        } else {
            return new OverloadsType(getOptions());
        }
    }

    @Override
    public Type testReplace(Map<Type, Type> replacements) {
        Type replacement = replacements.get(this);
        if (replacement != null) return replacement;

        return new OverloadableType(getOptions().stream().map(a -> a.testReplace(replacements)).collect(Collectors.toList()));
    }

    @Override
    public int serial() {
        return super.serial() + 1000;
    }
}
