package systems.monomer.execution;

import lombok.Getter;
import lombok.Setter;
import systems.monomer.syntaxtree.literals.StringNode;
import systems.monomer.syntaxtree.literals.StructureNode;
import systems.monomer.syntaxtree.literals.TupleNode;

@Getter @Setter
public abstract class Handler implements KeyInit, NodeInit, CommonInit {
    public static Handler init;

    //ignore the warnings here, methods of Handler are independent of the fields/completeness of the object
    private StringNode emptyString = stringNode("");
    private StructureNode emptyStructure = structureNode();
    private TupleNode emptyTuple = tupleNode();


    public StringNode emptyString() {
        return emptyString;
    }
    public StructureNode emptyStructure() {
        return emptyStructure;
    }
    public TupleNode emptyTuple() {
        return emptyTuple;
    }
}
