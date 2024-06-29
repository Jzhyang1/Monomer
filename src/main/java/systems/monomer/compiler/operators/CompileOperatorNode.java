package systems.monomer.compiler.operators;

import lombok.Setter;
import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileNode;
import systems.monomer.compiler.CompileSize;
import systems.monomer.syntaxtree.operators.GenericOperatorNode;
import systems.monomer.syntaxtree.operators.OperatorNode;
import systems.monomer.types.Type;

import java.util.function.BiFunction;
import java.util.function.Function;

public class CompileOperatorNode extends GenericOperatorNode implements CompileNode {
    @Setter
    private BiFunction<CompileOperatorNode, AssemblyFile, Operand> compileGenerator;

    public CompileOperatorNode(String name, Function<OperatorNode, Type> typeGenerator) {
        super(name, typeGenerator);
    }

    @Override
    public Operand compileValue(AssemblyFile file) {
        return null;
    }

    @Override
    public CompileSize compileSize() {
        return null;
    }

    @Override
    public void compileVariables(AssemblyFile file) {

    }
}
