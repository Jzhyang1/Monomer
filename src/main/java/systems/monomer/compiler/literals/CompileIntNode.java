package systems.monomer.compiler.literals;

import systems.monomer.compiler.CompileNode;
import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.output.CompileValue;
import systems.monomer.syntaxtree.literals.IntNode;

public class CompileIntNode extends IntNode implements CompileNode {
    public CompileIntNode(Integer i) {
        super(i);
    }

    @Override
    public CompileValue compile(CompileOutput output) {
        return null;
    }
}
