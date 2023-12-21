package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.types.Type;

import java.util.function.BiFunction;
import java.util.function.Function;

public class GenericOperatorNode extends OperatorNode {
    private final Function<GenericOperatorNode, ? extends InterpretResult> interpretGenerator;
    private final BiFunction<GenericOperatorNode, AssemblyFile, Operand> compileGenerator;
    private final Function<GenericOperatorNode, Type> typeGenerator;

    public GenericOperatorNode(
            String name,
            Function<GenericOperatorNode, ? extends InterpretResult> interpretGenerator,
            BiFunction<GenericOperatorNode, AssemblyFile, Operand> compileGenerator,
            Function<GenericOperatorNode, Type> typeGenerator
    ) {
        super(name);
        this.interpretGenerator = interpretGenerator;
        this.compileGenerator = compileGenerator;
        this.typeGenerator = typeGenerator;
    }

    public void matchTypes() {
        super.matchTypes();
        if(typeGenerator == null) {
            syntaxError("Unimplemented operator " + getName());
        }

        Type type = this.typeGenerator.apply(this);
        if(type != null) setType(type);
    }

    public CompileSize compileSize() {
        return getType().compileSize();
    }

    public Operand compileValue(AssemblyFile file) {
        return compileGenerator.apply(this, file);
    }

    public InterpretResult interpretValue() {
        return interpretGenerator.apply(this);
    }
}
