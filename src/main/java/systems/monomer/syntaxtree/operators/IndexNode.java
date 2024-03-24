package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.*;
import systems.monomer.types.*;
import systems.monomer.util.Pair;
import systems.monomer.variables.VariableKey;

import java.util.ArrayList;
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
        if (!getSecond().getType().equals(NumberType.INTEGER) && !(getSecond().getType() instanceof InterpretRanges)) //TODO replace Instanceof check
            throw syntaxError("Cannot index with type " + getSecond().getType());

        //TODO something like .unwrapped() below
        Type elementType = ((CollectionType) getFirst().getType()).getElementType();
        if (SequenceType.isSequence(elementType))
            setType(((CollectionType) elementType).getElementType());
        else
            setType(elementType);
    }


    private static abstract class IndexedPosition extends InterpretVariable {
        protected final List<InterpretValue> valueList;
        public IndexedPosition(List<InterpretValue> valueList) {
            this.valueList = valueList;
        }
        public abstract InterpretValue getValue();
        public abstract void setValue(InterpretValue value);

        @Override public String valueString() {return getValue().valueString();}
        @Override public CompileSize compileSize() {return null;}
    }

    private static class IndexedNumber extends IndexedPosition {
        private final int index;
        public IndexedNumber(List<InterpretValue> valueList, int index) {
            super(valueList);
            this.index = index;
        }
        public InterpretValue getValue() {
            return valueList.get(index);
        }
        public void setValue(InterpretValue value) {
            valueList.set(index, value);
        }
    }

    private class IndexedRanges extends IndexedPosition {
        private final InterpretRanges range;
        public IndexedRanges(List<InterpretValue> valueList, InterpretRanges range) {
            super(valueList); this.range = range;
        }
        public InterpretValue getValue() {
            List<InterpretValue> values = new ArrayList<>();
            for (InterpretValue index : range) {
                Number numIndex = index.getValue();
                int intIndex = numIndex.intValue();
                if (intIndex < 0 || intIndex >= valueList.size())
                    throw runtimeError("Index " + intIndex + " out of bounds for list of size " + valueList.size());
                values.add(valueList.get(intIndex));
            }
            return new InterpretList(values);
        }
        public void setValue(InterpretValue value) {
            if (value instanceof InterpretList list) {
                List<InterpretValue> values = list.getValues();
                if (values.size() != range.size())
                    throw runtimeError("Cannot set range of size " + range.size() + " with " + values.size() + " values");

                int i = 0;
                for (InterpretValue index : range) {
                    Number numIndex = index.getValue();
                    int intIndex = numIndex.intValue();
                    if (intIndex < 0 || intIndex >= valueList.size())
                        throw runtimeError("Index " + intIndex + " out of bounds for list of size " + valueList.size());
                    valueList.set(intIndex, values.get(i++));
                }
            }
            else
                throw runtimeError("Cannot set range with " + value);
        }
    }

    private IndexedPosition getIndexing() {
        InterpretResult first = getFirst().interpretValue();
        if(!first.isValue()) throw syntaxError("Can not index first value");
        InterpretResult second = getSecond().interpretValue();
        if(!second.isValue()) throw syntaxError("Second value is not an index");

        if (first instanceof InterpretList collection) {
            List<InterpretValue> valueList = collection.getValues();
            if (second instanceof InterpretNumber<?> number) {
                int intIndex = number.getValue().intValue();

                if (intIndex < 0 || intIndex >= valueList.size())
                    throw syntaxError("Index " + intIndex + " out of bounds for list of size " + valueList.size());

                return new IndexedNumber(valueList, intIndex);
            }
            else if (second instanceof InterpretRanges range) {
                return new IndexedRanges(valueList, range);
            }
            else throw syntaxError("Cannot index with " + second);
        }
        else
            throw syntaxError("Cannot index " + first);
    }

    @Override
    public InterpretValue interpretValue() {
        return getIndexing().getValue();
    }

    @Override
    public InterpretVariable interpretVariable() {
        return getIndexing();
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
