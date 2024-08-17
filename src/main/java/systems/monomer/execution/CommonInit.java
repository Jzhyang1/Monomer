package systems.monomer.execution;

import systems.monomer.syntaxtree.literals.StringNode;
import systems.monomer.syntaxtree.literals.StructureNode;
import systems.monomer.syntaxtree.literals.TupleNode;

/**
 * for commonly used initializations
 */
public interface CommonInit {
    public StringNode emptyString();
    public StructureNode emptyStructure();
    public TupleNode emptyTuple();
}
