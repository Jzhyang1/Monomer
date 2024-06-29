package systems.monomer.compiler;

import systems.monomer.compiler.Assembly.Instruction;
import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.operators.CompileOperatorNode;
import systems.monomer.types.NumberType;

import static systems.monomer.compiler.Assembly.Instruction.*;
import static systems.monomer.compiler.Assembly.Register.*;

//TODO move things over to here
public class ArithmeticAssembly {
    public static Operand compileNumericalBinary(AssemblyFile file, CompileOperatorNode self, Instruction intInstruction, Instruction floatInstruction) {
        return compileNumericalBinary(file, self, () -> {
            file.add(intInstruction, RBX.toOperand(), RDX.toOperand())
                    .add(MOV, RDX.toOperand(), RAX.toOperand());
        }, () -> {
            file.add(floatInstruction, RBX.toOperand(), RDX.toOperand())
                    .add(MOV, RDX.toOperand(), RAX.toOperand());
        });
    }

    /**
     * Compiles a numerical binary operation, and calls the appropriate callback based on the types of the operands.
     * The first operand will be in RDX, and the second operand will be in RBX.
     * The result should always be in RAX.
     *
     * @param file
     * @param self
     * @param ifBothIntegers
     * @param ifFloat
     */
    public static Operand compileNumericalBinary(AssemblyFile file, CompileOperatorNode self, Runnable ifBothIntegers, Runnable ifFloat) {
        Operand first = self.getFirstCompileNode().compileValue(file);
        //TODO optimize by checking for if possible to store in registers w/o being overwritten
        //TODO check all compileValue functions to make sure that all volatile data is stored
        //TODO all integers/doubles will be stored in registers, and unused values will be popped before returning
        file.push(RDX.toOperand())
                .mov(first, RDX.toOperand());
        Operand second = self.getSecondCompileNode().compileValue(file);
        file.mov(second, RBX.toOperand());

        if (self.getFirst().getType().equals(NumberType.INTEGER) && self.getSecond().getType().equals(NumberType.INTEGER)) {
            ifBothIntegers.run();
        } else {
            ifFloat.run();
        }

        file.pop(RDX.toOperand());
        return RAX.toOperand();
    }


    public static Operand addOrPos(CompileOperatorNode self, AssemblyFile file) {
        Operand first = self.getFirstCompileNode().compileValue(file);
        if (self.size() == 1)
            return first;

       return compileNumericalBinary(file, self, IADD, FADD);
    }

    public static Operand subOrNeg(CompileOperatorNode self, AssemblyFile file) {
        if (self.size() == 1) {
            Operand first = self.getFirstCompileNode().compileValue(file);
            file.add(MOV, first, new Operand(Operand.Type.REGISTER, RAX, 0, 0));
            if (self.getFirst().getType().equals(NumberType.INTEGER)) {
                file.add(INEG, null, RDX.toOperand());
            } else {
                file.add(FNEG, null, RDX.toOperand());
            }
            return RAX.toOperand();
        }
        return compileNumericalBinary(file, self, ISUB, FSUB);
    }
}
