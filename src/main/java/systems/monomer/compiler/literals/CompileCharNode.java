package systems.monomer.compiler.literals;

import systems.monomer.compiler.CompileNode;
import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.output.CompileValue;
import systems.monomer.syntaxtree.literals.CharNode;

public class CompileCharNode extends CharNode implements CompileNode {
    public CompileCharNode(Character c) {
        super(c);
    }

    @Override
    public CompileValue compile(CompileOutput output) {
        return null;
    }
}
