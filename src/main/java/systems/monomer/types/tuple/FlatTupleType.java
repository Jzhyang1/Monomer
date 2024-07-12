package systems.monomer.types.tuple;

import systems.monomer.interpreter.values.InterpretTuple;
import systems.monomer.types.Type;

import java.util.List;

public class FlatTupleType extends TupleType {
    public FlatTupleType() {
    }

    public FlatTupleType(List<Type> sequence) {
        super(sequence);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof FlatTupleType ftt && sequence.equals(ftt.sequence);
    }
}
