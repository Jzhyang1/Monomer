package systems.monomer.types.function;


import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.types.Type;
import systems.monomer.types.pseudo.UnionType;
import systems.monomer.types.signature.Signature;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static systems.monomer.errorhandling.ErrorBlock.programError;

//TODO it is inefficient to store FunctionBodies in Signatures within the OverloadedFunctionType because it will require
// some O(n) time to search for the FunctionBody for each Signature, and for some commonly used, heavily overloaded functions
// (eg. convert) this is non-negligible.
// Instead, on the call to simplify, return a new object OverloadsType with _options_ in a sorted List to support O(log n) lookup
// and O(1) access

//TODO also rename this to OverloadableType
public class OverloadedFunctionType extends UnionType {
    public OverloadedFunctionType() {
    }

    public OverloadedFunctionType(List<? extends Type> options) {
        super(options);
    }



    @Override
    protected OverloadedFunctionType simplifyOptions() {
        getOptions().sort(null);    //most inclusive types to the left
        return this;
    }

    @Override
    public Type simplify() {
        //simplify all component types
        getOptions().replaceAll(Type::simplify);

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

        return new OverloadedFunctionType(getOptions().stream().map(a -> a.testReplace(replacements)).collect(Collectors.toList()));
    }

    @Override
    public int serial() {
        return super.serial() + 1000;
    }
}
