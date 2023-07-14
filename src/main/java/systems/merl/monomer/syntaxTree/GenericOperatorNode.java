package systems.merl.monomer.syntaxTree;

import systems.merl.monomer.compiler.CompileSize;
import systems.merl.monomer.compiler.CompileValue;
import systems.merl.monomer.interpreter.InterpretValue;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GenericOperatorNode extends OperatorNode{
    private Function<GenericOperatorNode,InterpretValue> interpret;
    private Function<GenericOperatorNode, CompileValue> compile;
    private CompileSize compileSize;

    public GenericOperatorNode(String name){
        super(name);
    }

    public void setCompile(Function<GenericOperatorNode, CompileValue> compile) {
        this.compile = compile;
    }
    public void setCompileSize(CompileSize compileSize) {
        this.compileSize = compileSize;
    }
    public void setInterpret(Function<GenericOperatorNode, InterpretValue> interpret) {
        this.interpret = interpret;
    }

    public void matchTypes() {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        return compileSize;
    }
    public CompileValue compileValue() {
        return compile.apply(this);
    }

    public InterpretValue interpretValue() {
        return interpret.apply(this);
    }
}
