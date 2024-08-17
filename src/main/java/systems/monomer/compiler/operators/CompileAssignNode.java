package systems.monomer.compiler.operators;

import systems.monomer.compiler.CompileNode;
import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.output.CompileValue;
import systems.monomer.syntaxtree.operators.AssignNode;

public class CompileAssignNode extends AssignNode implements CompileNode {
    @Override
    public CompileValue compile(CompileOutput output) {
        return null;
    }
}
