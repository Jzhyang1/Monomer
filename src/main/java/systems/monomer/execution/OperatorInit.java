package systems.monomer.execution;

import systems.monomer.syntaxtree.operators.GenericOperatorNode;

import java.util.function.Function;

public interface OperatorInit<T extends Function<GenericOperatorNode, ?>> {
    T getOperatorBody(String symbol);
}
