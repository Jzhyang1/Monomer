package systems.monomer.compiler.operators;

import lombok.Setter;
import systems.monomer.compiler.CompileNode;
import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.output.CompileValue;
import systems.monomer.syntaxtree.operators.GenericOperatorNode;
import systems.monomer.syntaxtree.operators.OperatorNode;
import systems.monomer.types.Type;

import java.util.function.BiFunction;
import java.util.function.Function;

public class CompileOperatorNode extends GenericOperatorNode implements CompileNode {
    @Setter
    private Function<GenericOperatorNode, BiFunction<CompileOperatorNode, CompileOutput, CompileValue>> compileGenerator;

    private BiFunction<CompileOperatorNode, CompileOutput, CompileValue> compile;

    public CompileOperatorNode(String name, Function<OperatorNode, Type> typeGenerator) {
        super(name, typeGenerator);
    }

    @Override
    public void matchTypes() {
        super.matchTypes();
        compile = compileGenerator.apply(this);
    }

    @Override
    public CompileValue compile(CompileOutput output) {
        return compile.apply(this, output);
    }
}
