package systems.monomer.types.pseudo;


import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.Type;

import static systems.monomer.errorhandling.ErrorBlock.programError;

public abstract class PseudoType implements Type {

    @Override
    public boolean typeContains(Type other) {
        return this == other;
    }

    @Override
    public int serial() {
        return 6400_000;
    }

    @Override
    public InterpretValue defaultValue() {
        throw programError("PseudoType does not have a defaultValue", ErrorBlock.Reason.SYNTAX);
    }
}
