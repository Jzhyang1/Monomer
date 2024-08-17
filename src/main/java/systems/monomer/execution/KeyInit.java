package systems.monomer.execution;

import systems.monomer.syntaxtree.operators.IndexNode;
import systems.monomer.variables.FieldKey;
import systems.monomer.variables.Key;
import systems.monomer.variables.VariableKey;

public interface KeyInit {
    VariableKey variableKey();
    FieldKey fieldKey(String name, Key parent);
    Key indexKey(IndexNode owner);
}
