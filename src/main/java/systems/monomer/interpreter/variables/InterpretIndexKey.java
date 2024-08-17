package systems.monomer.interpreter.variables;

import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.operators.InterpretIndexNode;
import systems.monomer.interpreter.values.InterpretCollection;
import systems.monomer.syntaxtree.operators.IndexNode;
import systems.monomer.util.Pair;
import systems.monomer.variables.IndexKey;

public class InterpretIndexKey extends IndexKey implements InterpretVariable {
    public InterpretIndexKey(IndexNode owner) {
        super(owner);
    }

    private Pair<InterpretCollection, InterpretValue> getIndexingParam() {
        InterpretIndexNode owner = (InterpretIndexNode) getOwner();
        InterpretResult first = owner.getFirstInterpretNode().interpretValue();
        if(!first.isValue()) throw owner.syntaxError("Can not index first value");
        InterpretResult second = owner.getSecondInterpretNode().interpretValue();
        if(!second.isValue()) throw owner.syntaxError("Second value is not an index");

        InterpretCollection collection = (InterpretCollection) first.asValue();
        InterpretValue index = second.asValue();
        return new Pair<>(collection, index);
    }

    public InterpretValue getValue() {
        Pair<InterpretCollection, InterpretValue> indexing = getIndexingParam();
        return indexing.getFirst().getValueAt(indexing.getSecond().getValue());
    }
    public void setValue(InterpretValue value) {
        Pair<InterpretCollection, InterpretValue> indexing = getIndexingParam();
        indexing.getFirst().setValueAt(indexing.getSecond().getValue(), value);
    }

    @Override
    public void lock() {
        //TODO this shouldn't be called
    }

    @Override
    public boolean isLocked() {
        return false; //TODO check if parent list is constant
    }
}
