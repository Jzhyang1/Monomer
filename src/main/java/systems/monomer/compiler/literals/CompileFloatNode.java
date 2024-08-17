package systems.monomer.compiler.literals;

import systems.monomer.compiler.CompileNode;
import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.output.CompileValue;
import systems.monomer.syntaxtree.literals.FloatNode;

public class CompileFloatNode extends FloatNode implements CompileNode {

    public CompileFloatNode(Double f) {
        super(f);
    }

    @Override
    public CompileValue compile(CompileOutput output) {
        return null;
    }
}
