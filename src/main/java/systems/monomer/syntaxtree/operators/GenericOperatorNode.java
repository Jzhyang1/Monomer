package systems.monomer.syntaxtree.operators;

import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.TypeContext;
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

    public void matchTypes() {
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
    }


    private static class TypeTestOperatorNode extends OperatorNode {
        public TypeTestOperatorNode(String name) {
            super(name);
        }
        public TypeTestOperatorNode(String name, Type type) {
            super(name);
            super.setType(type);
        }
    }
    @Override
    public Type testType(TypeContext context) {
        TypeTestOperatorNode testNode = new TypeTestOperatorNode(getName());
        for (Node child : getChildren()) {
            testNode.add(new TypeTestOperatorNode(child.getName(), child.testType(context)));
        }
        return typeGenerator.apply(testNode);
    }
}
