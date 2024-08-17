package systems.monomer.compiler.operators;

import systems.monomer.compiler.CompileNode;
import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.output.CompileValue;
import systems.monomer.syntaxtree.operators.AssertTypeNode;

public class CompileAssertTypeNode extends AssertTypeNode implements CompileNode {
    @Override
    public CompileValue compile(CompileOutput output) {
        return null;
    }
}
