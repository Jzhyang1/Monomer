package systems.monomer.compiler.controls;

import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.output.CompileValue;
import systems.monomer.syntaxtree.controls.ElseNode;

public class CompileElseNode extends ElseNode implements CompileControlNode {
    @Override
    public CompileValue compile(CompileOutput output) {
        return null;
    }
}
