package systems.monomer.interpreter.operators;

import lombok.Setter;
import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.syntaxtree.operators.GenericOperatorNode;
import systems.monomer.syntaxtree.operators.OperatorNode;
import systems.monomer.types.Type;

import java.util.function.Function;

public class InterpretOperatorNode extends GenericOperatorNode implements InterpretNode {
    @Setter
    private Function<InterpretOperatorNode, ? extends InterpretResult> interpretGenerator;

    public InterpretOperatorNode(
            String name,
            Function<OperatorNode, Type> typeGenerator
    ) {
        super(name, typeGenerator);
    }

    @Override
    public InterpretVariable interpretVariable() {
        throw runtimeError("Cannot assign to result of operator " + getName());
    }

    @Override
    public InterpretResult interpretValue() {
        return interpretGenerator.apply(this);
    }
}
