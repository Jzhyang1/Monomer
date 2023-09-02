package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretCollection;
import systems.monomer.interpreter.InterpretList;
import systems.monomer.interpreter.InterpretNumber;
import systems.monomer.interpreter.InterpretValue;
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
        InterpretValue value = getFirst().interpretValue();
        InterpretValue index = getSecond().interpretValue();

        if (value instanceof InterpretList collection &&
                index instanceof InterpretNumber<?> number) {
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
