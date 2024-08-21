package systems.monomer.execution;

import lombok.Getter;
import lombok.Setter;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.operators.GenericOperatorNode;
import systems.monomer.tokenizer.Operators;

import java.util.function.Function;

@SuppressWarnings({"ThisEscapedInObjectConstruction", "OverridableMethodCallDuringObjectConstruction", "OverriddenMethodCallDuringObjectConstruction"})
@Getter @Setter
public abstract class Initializer<T extends Function<GenericOperatorNode, ?>> implements KeyInit, NodeInit, CommonInit, OperatorInit<T> {
    //ignore the warnings here, methods of Handler are independent of the fields/completeness of the object
    private final Node emptyString = stringNode("").with(this);
    private final Node emptyStructure = structureNode().with(this);
    private final Node emptyTuple = tupleNode().with(this);

    public final Operators operators = new Operators().with(this);

    public Node emptyString() {
        return emptyString;
    }
    public Node emptyStructure() {
        return emptyStructure;
    }
    public Node emptyTuple() {
        return emptyTuple;
    }
}
