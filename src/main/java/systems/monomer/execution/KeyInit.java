package systems.monomer.execution;

import systems.monomer.syntaxtree.operators.IndexNode;
import systems.monomer.variables.Key;

public interface KeyInit {
    Key variableKey();
    Key fieldKey(String name, Key parent);
    Key indexKey(IndexNode owner);
}
