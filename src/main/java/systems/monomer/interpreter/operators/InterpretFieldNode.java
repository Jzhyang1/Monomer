package systems.monomer.interpreter.operators;

import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.variables.InterpretVariable;
import systems.monomer.syntaxtree.operators.FieldNode;

public class InterpretFieldNode extends FieldNode implements InterpretNode {
    public InterpretVariable interpretVariable() {
        if(variableKey == null) throw runtimeError("Cannot assign to field of non-variable");

        return (InterpretVariable) variableKey;
    }
    public InterpretResult interpretValue() {
        if(variableKey != null) return ((InterpretVariable) variableKey).getValue();

        InterpretNode first = getFirstInterpretNode();

        InterpretResult firstResult = first.interpretValue();
        if(!firstResult.isValue()) return firstResult;

        return firstResult.asValue().getField(fieldName);
    }
}
