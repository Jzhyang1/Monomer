package systems.monomer.execution;

import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.syntaxtree.operators.GenericOperatorNode;

import java.util.function.Function;

//It seems a bit weird for Initialized to extend ErrorBlock, but Initialized
// adds onto the set of fields that need to be initialized for functionality, similar to ErrorBlock;
//This is also needed for Node to extend both Initialized and ErrorBlock
@SuppressWarnings("unchecked")
public class Initialized<T> {
    protected Initializer env;

    public T with(Initializer env) {
        this.env = env;
        return (T) this;
    }
}
