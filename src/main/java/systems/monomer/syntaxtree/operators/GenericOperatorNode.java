package systems.monomer.syntaxtree.operators;

import systems.monomer.syntaxtree.Node;
import systems.monomer.types.Type;

import java.util.function.Function;

public class GenericOperatorNode extends OperatorNode {
    private final Function<OperatorNode, Type> typeGenerator;

    public GenericOperatorNode(
            String name,
            Function<OperatorNode, Type> typeGenerator
    ) {
        super(name);
        this.typeGenerator = typeGenerator;
    }

    public Node matchTypes() {
        super.matchTypes();
        if(typeGenerator == null) {
            throw syntaxError("Unimplemented operator " + getName());
        }

        try {
            Type type = this.typeGenerator.apply(this);
            if(type != null) setType(type);
        } catch (ProgramErrorException ex) {
            throw rethrowError(ex);
        }
        return this;
    }
}
