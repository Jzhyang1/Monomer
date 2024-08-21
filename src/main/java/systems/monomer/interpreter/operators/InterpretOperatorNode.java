package systems.monomer.interpreter.operators;

import org.jetbrains.annotations.Nullable;
import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.variables.InterpretVariable;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.operators.GenericOperatorNode;
import systems.monomer.syntaxtree.operators.OperatorNode;
import systems.monomer.types.Type;

import java.util.Iterator;
import java.util.function.Function;

public class InterpretOperatorNode extends GenericOperatorNode implements InterpretNode {
    private Function<Iterator<InterpretValue>, ? extends InterpretResult> interpret;

    public InterpretOperatorNode(
            String name,
            Function<OperatorNode, Type> typeGenerator
    ) {
        super(name, typeGenerator);
    }

    @Override
    public void matchTypes() {
        super.matchTypes();
        //TODO this is ugly but passing the type without erasure is hard;
        // instead, make the signatures for interpret and compile identical to avoid generics
        interpret = (Function<Iterator<InterpretValue>, ? extends InterpretResult>)
                env.getOperatorBody(getName()).apply(this);
    }

    @Override
    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot assign to result of operator " + getName());
    }



    @Override
    public InterpretResult interpretValue() {
        OperandIterator iter = new OperandIterator(this);
        InterpretResult res = interpret.apply(iter);
        if(iter.current() != null && !iter.current().isValue()) {
            return iter.current();
        }
        return res;
    }


    private static final class OperandIterator implements Iterator<InterpretValue> {
        private final Iterator<Node> children;
        private @Nullable InterpretResult queued = null;

        public OperandIterator(InterpretOperatorNode parent) {
            children = parent.getChildren().iterator();
            loadQueue();
        }

        private void loadQueue() {
            if(!children.hasNext()) {
                queued = null;
                return;
            }

            Node n = children.next();
            if(!(n instanceof InterpretNode interpretNode))
                throw programError("Interpret mode \"InterpretOperatorNode\" used outside of interpret context", Reason.OTHER);
            queued = interpretNode.interpretValue();
        }

        @Override
        public boolean hasNext() {
            return queued != null && queued.isValue();
        }

        public InterpretResult current() {
            return queued;
        }

        @Override
        public InterpretValue next() {
            InterpretValue ret = queued.asValue();
            loadQueue();
            return ret;
        }
    }
}
