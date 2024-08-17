package systems.monomer.compiler.literals;

import systems.monomer.compiler.CompileNode;
import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.output.CompileValue;
import systems.monomer.syntaxtree.literals.RangeNode;

public class CompileRangeNode extends RangeNode implements CompileNode {
    public CompileRangeNode(boolean startInclusive, boolean stopInclusive) {
        super(startInclusive, stopInclusive);
    }

    @Override
    public CompileValue compile(CompileOutput output) {
        return null;
    }
}
