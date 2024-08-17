package systems.monomer.compiler.operators;

import systems.monomer.compiler.CompileNode;
import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.output.CompileValue;
import systems.monomer.syntaxtree.operators.CallNode;

public class CompileCallNode extends CallNode implements CompileNode {
    @Override
    public CompileValue compile(CompileOutput output) {
        return null;
    }
}
