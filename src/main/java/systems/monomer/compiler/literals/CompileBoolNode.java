package systems.monomer.compiler.literals;

import systems.monomer.compiler.CompileNode;
import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.output.CompileValue;
import systems.monomer.syntaxtree.literals.BoolNode;

public class CompileBoolNode extends BoolNode implements CompileNode {
    public CompileBoolNode(boolean value) {
        super(value);
    }

    @Override
    public CompileValue compile(CompileOutput output) {
        return null;
    }
}
