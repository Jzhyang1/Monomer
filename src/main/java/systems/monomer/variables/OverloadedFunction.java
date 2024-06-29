package systems.monomer.variables;

import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.OverloadedFunctionType;
import systems.monomer.types.Signature;
import systems.monomer.util.PairList;

public class OverloadedFunction extends OverloadedFunctionType implements InterpretValue {
    public OverloadedFunction(PairList<Signature, FunctionBody> overloads) {
        getOverloads().addAll(overloads);
    }


    @Override
    public OverloadedFunction clone() {
        return (OverloadedFunction) super.clone();
    }

    @Override
    public String toString() {
        return "OverloadedFunction{" +
                "overloads=" + getOverloads() +
                '}';
    }
}
