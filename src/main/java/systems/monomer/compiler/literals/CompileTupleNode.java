package systems.monomer.compiler.literals;

import systems.monomer.compiler.CompileNode;
import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.output.CompileValue;
import systems.monomer.syntaxtree.literals.TupleNode;

public class CompileTupleNode extends TupleNode implements CompileNode {
    public CompileTupleNode(){}
    public CompileTupleNode(String name){
        super(name);
    }

    @Override
    public CompileValue compile(CompileOutput output) {
        return null;
    }
}
