package systems.monomer.compiler.literals;

import systems.monomer.compiler.CompileNode;
import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.output.CompileValue;
import systems.monomer.syntaxtree.literals.StringNode;

public class CompileStringNode extends StringNode implements CompileNode {

    public CompileStringNode(String s) {
        super(s);
    }

    @Override
    public CompileValue compile(CompileOutput output) {
        return null;
    }
}
