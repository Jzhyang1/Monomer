package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.TypeContext;
import systems.monomer.types.Type;

import java.util.function.BiFunction;
import java.util.function.Function;

public class GenericOperatorNode extends OperatorNode {
    private final Function<GenericOperatorNode, ? extends InterpretResult> interpretGenerator;
    private final BiFunction<GenericOperatorNode, AssemblyFile, Operand> compileGenerator;
    private final Function<OperatorNode, Type> typeGenerator;

    public GenericOperatorNode(
            String name,
            Function<GenericOperatorNode, ? extends InterpretResult> interpretGenerator,
            BiFunction<GenericOperatorNode, AssemblyFile, Operand> compileGenerator,
            Function<OperatorNode, Type> typeGenerator
    ) {
        super(name);
        this.interpretGenerator = interpretGenerator;
        this.compileGenerator = compileGenerator;
        this.typeGenerator = typeGenerator;
    }

    public void matchTypes() {
        super.matchTypes();
        if(typeGenerator == null) {
            throw syntaxError("Unimplemented operator " + getName());
        }

        Type type = this.typeGenerator.apply(this);
        if(type != null) setType(type);
    }


    private static class TypeTestOperatorNode extends OperatorNode {
        public TypeTestOperatorNode(String name) {
            super(name);
        }
        public TypeTestOperatorNode(String name, Type type) {
            super(name);
            super.setType(type);
        }

        @Override public InterpretResult interpretValue() {
            throw syntaxError("can not interpret type test operator " + getName());
        }
        @Override public Operand compileValue(AssemblyFile file) {
            throw syntaxError("can not compile type test operator " + getName());
        }
        @Override public CompileSize compileSize() {
            throw syntaxError("can not compile type test operator " + getName());
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

    public CompileSize compileSize() {
        return getType().compileSize();
    }

    public Operand compileValue(AssemblyFile file) {
        return compileGenerator.apply(this, file);
    }

    public InterpretResult interpretValue() {
        return checkedResult(interpretGenerator.apply(this));
    }
}
