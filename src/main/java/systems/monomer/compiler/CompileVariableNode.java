package systems.monomer.compiler;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.syntaxtree.VariableNode;

import static systems.monomer.compiler.Assembly.Register.EBP;

public class CompileVariableNode extends VariableNode implements CompileNode {
    public CompileVariableNode(String name) {
        super(name);
    }

    @Override
    public void compileVariables(AssemblyFile file) {
        if(variableKey.getAddress() != null) return;
        if (variableKey.getType().isConstant())
            variableKey.setAddress(new Operand(
                    Operand.Type.MEMORY,
                    EBP,
                    file.incrementStackPosition(compileSize().getConstantSize()),
                    0));
        else
            variableKey.setAddress(new Operand(
                    Operand.Type.MEMORY_OF_POINTER,
                    EBP,
                    file.incrementStackPosition(Operand.SIZE_POINTER_SIZE),
                    0));
    }

    public Operand compileValue(AssemblyFile file) {
//        System.out.println("compiling variable " + getName() + " with address " + variableKey.getAddress(file));
        return variableKey.getAddress();
    }

    public CompileSize compileSize() {
        return variableKey.compileSize();
    }
}
