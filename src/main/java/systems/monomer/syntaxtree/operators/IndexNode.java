package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.*;
import systems.monomer.types.CollectionType;
import systems.monomer.types.ListType;
import systems.monomer.types.NumberType;

import java.util.List;

public class IndexNode extends OperatorNode {
    public IndexNode() {
        super("index");
    }

    @Override
    public void matchTypes() {
        super.matchTypes();
        if (!(getFirst().getType() instanceof ListType))    //TODO add support for other collection types
            syntaxError("Cannot index non-collection type " + getFirst().getType());
        if (!(getSecond().getType() instanceof NumberType<?> num &&
                num.getValue() instanceof Integer)) //TODO replace Instanceof Integer check with getTypeName check
            syntaxError("Cannot index with non-integer type " + getSecond().getType());

        setType(((CollectionType) getFirst().getType()).getElementType());
    }

    @Override
    public InterpretValue interpretValue() {
        InterpretResult firstResult = getFirst().interpretValue();
        if(!firstResult.isValue()) syntaxError("Can not index first value");
        InterpretResult secondResult = getSecond().interpretValue();
        if(!secondResult.isValue()) syntaxError("Second value is not an index");

        InterpretValue value = firstResult.asValue(), index = secondResult.asValue();

        if (value instanceof InterpretList collection &&
                index instanceof InterpretNumber<?> number) {
            List<InterpretValue> valueList = collection.getValues();
            int intIndex = number.getValue().intValue();

            if(intIndex < 0 || intIndex >= valueList.size())
                syntaxError("Index " + number.getValue().intValue() + " out of bounds for list of size " + collection.size());

            return valueList.get(intIndex);
        }
        else {
            syntaxError("Cannot index " + value + " with " + index);
            return null;
        }
    }

    public Operand compileValue(AssemblyFile file) {
        throw new Error("TODO unimplemented");
    }

    @Override
    public CompileSize compileSize() {
        return null;
    }
}
