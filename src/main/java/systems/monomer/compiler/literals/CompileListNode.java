package systems.monomer.compiler.literals;

import systems.monomer.compiler.CompileNode;
import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.output.CompileValue;
import systems.monomer.syntaxtree.literals.ListNode;

public class CompileListNode extends ListNode implements CompileNode {

    @Override
    public CompileValue compile(CompileOutput output) {
        return null;
    }
}
