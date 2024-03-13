package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.*;
import systems.monomer.types.*;
import systems.monomer.util.Pair;
import systems.monomer.variables.VariableKey;

import java.util.List;

public class IndexNode extends OperatorNode {
    public IndexNode() {
        super("index");
    }

    @Override
    public void matchTypes() {
        super.matchTypes();
        if (!ListType.LIST.typeContains(getFirst().getType()))    //TODO add support for other collection types
            throw syntaxError("Cannot index non-collection type " + getFirst().getType());
        if (!(getSecond().getType() instanceof NumberType<?> num &&
                num.getValue() instanceof Integer)) //TODO replace Instanceof Integer check with getTypeName check
            throw syntaxError("Cannot index with non-integer type " + getSecond().getType());

        //TODO something like .unwrapped() below
        Type elementType = ((CollectionType) getFirst().getType()).getElementType();
        if (SequenceType.isSequence(elementType))
            setType(((CollectionType) elementType).getElementType());
        else
            setType(elementType);
    }

    private Pair<List<InterpretValue>, Number> getIndexing() {
        InterpretResult first = getFirst().interpretValue();
        if(!first.isValue()) throw syntaxError("Can not index first value");
        InterpretResult second = getSecond().interpretValue();
        if(!second.isValue()) throw syntaxError("Second value is not an index");

        if (first instanceof InterpretList collection &&
                second instanceof InterpretNumber<?> number) {
            int intIndex = number.getValue().intValue();
            List<InterpretValue> valueList = collection.getValues();

            if(intIndex < 0 || intIndex >= valueList.size())
                throw syntaxError("Index " + intIndex + " out of bounds for list of size " + valueList.size());

            return new Pair<>(valueList, intIndex);
        }
        else
            throw syntaxError("Cannot index " + first + " with " + second);
    }

    @Override
    public InterpretValue interpretValue() {
        Pair<List<InterpretValue>, Number> indexing = getIndexing();

        List<InterpretValue> valueList = indexing.getFirst();
        int intIndex = indexing.getSecond().intValue();

        return valueList.get(intIndex);
    }

    @Override
    public InterpretVariable interpretVariable() {
        return new InterpretVariable() {
            @Override
            public InterpretValue getValue() {
                return IndexNode.this.interpretValue();
            }

            @Override
            public void setValue(InterpretValue value) {
                Pair<List<InterpretValue>, Number> indexing = getIndexing();
                indexing.getFirst().set(indexing.getSecond().intValue(), value);
            }

            @Override
            public String valueString() {return null;}
            @Override
            public CompileSize compileSize() {return null;}
        };
    }

    @Override
    public VariableKey getVariableKey() {
        return new VariableKey() {
            @Override
            public void setType(Type type) {
                if (IndexNode.this.getType() == AnyType.ANY)
                    IndexNode.this.setType(type);
                else if (!IndexNode.this.getType().typeContains(type))
                    throw IndexNode.this.syntaxError("Type mismatch: " + IndexNode.this.getType() + " and " + type);
            }
        };
    }

    public Operand compileValue(AssemblyFile file) {
        throw new Error("TODO unimplemented");
    }

    @Override
    public CompileSize compileSize() {
        return null;
    }
}
