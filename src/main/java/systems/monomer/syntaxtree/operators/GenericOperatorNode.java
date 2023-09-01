package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.Type;

import java.util.function.Function;

public class GenericOperatorNode extends OperatorNode {
    private Function<GenericOperatorNode, InterpretValue> interpret;
    private Function<GenericOperatorNode, CompileValue> compile;
    private Function<GenericOperatorNode, Type> type;
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
    public void setType(Function<GenericOperatorNode, Type> type) {
        this.type = type;
    }

    public void matchTypes() {
        super.matchTypes();
        if(type == null) {
            throwError("Unimplemented operator " + getName());
        }

        Type type = this.type.apply(this);
        if(type != null) setType(type);
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
