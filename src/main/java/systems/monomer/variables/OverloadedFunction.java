package systems.monomer.variables;

import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.OverloadedFunctionType;
import systems.monomer.types.Signature;
import systems.monomer.types.Type;
import systems.monomer.util.Pair;
import systems.monomer.util.PairList;

public class OverloadedFunction extends OverloadedFunctionType implements InterpretValue {
    public OverloadedFunction(PairList<Signature, FunctionBody> overloads) {
        for(Pair<Signature, FunctionBody> pair : overloads) {
            super.putOverload(pair.getFirst(), pair.getSecond());
        }
    }


    @Override
    public OverloadedFunction clone() {
        return (OverloadedFunction) super.clone();
    }
}
