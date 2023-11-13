package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.*;
import systems.monomer.types.CollectionType;
import systems.monomer.types.ListType;
import systems.monomer.types.NumberType;

public class IndexNode extends OperatorNode {
    public IndexNode() {
        super("index");
    }

    @Override
    public void matchTypes() {
        super.matchTypes();
        if (!(getFirst().getType() instanceof ListType))    //TODO add support for other collection types
            throwError("Cannot index non-collection type " + getFirst().getType());
        if (!(getSecond().getType() instanceof NumberType<?> num &&
                num.getValue() instanceof Integer)) //TODO replace Instanceof Integer check with getTypeName check
            throwError("Cannot index with non-integer type " + getSecond().getType());
    }

    @Override
    public InterpretValue interpretValue() {
        InterpretResult firstr = getFirst().interpretValue();
        if(!firstr.isValue()) throw new RuntimeException("First value is not a value");
        InterpretResult secondr = getSecond().interpretValue();
        if(!secondr.isValue()) throw new RuntimeException("Second value is not a value");

        InterpretValue value = firstr.asValue(), index = secondr.asValue();

        if (value instanceof InterpretList collection &&
                index instanceof InterpretNumber<?> number) {
            if(number.getValue().intValue() < 0 || number.getValue().intValue() >= collection.getValues().size())
                throwError("Index " + number.getValue().intValue() + " out of bounds for list of size " + collection.getValues().size());

            return collection.getValues().get(number.getValue().intValue());
        }
        throwError("Cannot index " + value + " with " + index);
        return null;
    }

    @Override
    public CompileValue compileValue() {
        return null;
    }

    @Override
    public CompileSize compileSize() {
        return null;
    }
}
