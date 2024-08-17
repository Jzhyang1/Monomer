package systems.monomer.compiler.literals;

import systems.monomer.compiler.CompileNode;
import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.output.CompileValue;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.literals.StringBuilderNode;

import java.util.Collection;

public class CompileStringBuilderNode extends StringBuilderNode implements CompileNode {
    public CompileStringBuilderNode(Collection<? extends Node> list) {
        super(list);
    }

    @Override
    public CompileValue compile(CompileOutput output) {
        return null;
    }
}
