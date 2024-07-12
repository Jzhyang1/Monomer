package systems.monomer.compiler.operators;

import systems.monomer.compiler.assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileNode;
import systems.monomer.compiler.CompileSize;
import systems.monomer.errorhandling.UnimplementedError;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.operators.FieldNode;
import systems.monomer.types.Type;

public class CompileFieldNode extends FieldNode implements CompileNode {

    @Override
    public void compileVariables(AssemblyFile file) {
        Node first = getFirst();
        assert first instanceof CompileNode;

        ((CompileNode) first).compileVariables(file);
    }

    public Operand compileValue(AssemblyFile file) {
        if(variableKey != null)
            return variableKey.getAddress();

        Node first = getFirst();
        assert first instanceof CompileNode;

        Type firstType = first.getType();
        Operand firstLoc = ((CompileNode) first).compileValue(file);
        return new Operand(Operand.Type.MEMORY,
                firstLoc.register,
                firstLoc.offset + firstType.getFieldOffset(fieldName),
                0);
    }

    public CompileSize compileSize() {
        throw new UnimplementedError();
    }
}
