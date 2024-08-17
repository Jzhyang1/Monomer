package systems.monomer.compiler.operators;

import systems.monomer.compiler.CompileNode;
import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.output.CompileValue;
import systems.monomer.syntaxtree.operators.IndexNode;

import java.util.ArrayList;
import java.util.List;

public class CompileIndexNode extends IndexNode implements CompileNode {
    @Override
    public CompileValue compile(CompileOutput output) {
        return null;
    }
}
