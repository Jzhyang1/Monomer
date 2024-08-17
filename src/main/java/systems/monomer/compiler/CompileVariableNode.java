package systems.monomer.compiler;

import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.output.CompileValue;
import systems.monomer.syntaxtree.VariableNode;


public class CompileVariableNode extends VariableNode implements CompileNode {
    public CompileVariableNode(String name) {
        super(name);
    }

    @Override
    public CompileValue compile(CompileOutput file) {
//        if(variableKey.getAddress() != null) return null;
//        if (variableKey.getType().isConstant())
//            variableKey.setAddress(new Operand(
//                    Operand.Type.MEMORY,
//                    EBP,
//                    file.incrementStackPosition(compileSize().getConstantSize()),
//                    0));
//        else
//            variableKey.setAddress(new Operand(
//                    Operand.Type.MEMORY_OF_POINTER,
//                    EBP,
//                    file.incrementStackPosition(Operand.SIZE_POINTER_SIZE),
//                    0));
        return null;
    }

//    public Operand compileValue(AssemblyFile file) {
////        System.out.println("compiling variable " + getName() + " with address " + variableKey.getAddress(file));
//        return variableKey.getAddress();
//    }
//
//    public CompileSize compileSize() {
//        return variableKey.compileSize();
//    }
}
