package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.Type;

import java.util.function.Function;

public class GenericOperatorNode extends OperatorNode {
    private Function<GenericOperatorNode, ? extends InterpretResult> interpretGenerator;
    private Function<GenericOperatorNode, CompileValue> compileGenerator;
    private Function<GenericOperatorNode, Type> typeGenerator;

    public GenericOperatorNode(String name, Function<GenericOperatorNode, ? extends InterpretResult> interpretGenerator, Function<GenericOperatorNode, CompileValue> compileGenerator, Function<GenericOperatorNode, Type> typeGenerator) {
        super(name);
        this.interpretGenerator = interpretGenerator;
        this.compileGenerator = compileGenerator;
        this.typeGenerator = typeGenerator;
    }

    public void setCompileGenerator(Function<GenericOperatorNode, CompileValue> compileGenerator) {
        this.compileGenerator = compileGenerator;
    }
    public void setInterpretGenerator(Function<GenericOperatorNode, InterpretResult> interpretGenerator) {
        this.interpretGenerator = interpretGenerator;
    }
    public void setTypeGenerator(Function<GenericOperatorNode, Type> typeGenerator) {
        this.typeGenerator = typeGenerator;
    }

    public void matchTypes() {
        super.matchTypes();
        if(typeGenerator == null) {
            throwError("Unimplemented operator " + getName());
        }

        Type type = this.typeGenerator.apply(this);
        if(type != null) setType(type);
    }

    public CompileSize compileSize() {
        return getType().compileSize();
    }
    public CompileValue compileValue() {
        return compileGenerator.apply(this);
    }

    public InterpretResult interpretValue() {
        return interpretGenerator.apply(this);
    }
}
