package systems.monomer.compiler;

import systems.monomer.compiler.Assembly.Instruction;
import systems.monomer.compiler.Assembly.Operand;

public class AssemblyLine {
    private final Instruction instruction;
    private final Operand from, to;

    public AssemblyLine(Instruction instruction, Operand from, Operand to) {
        this.instruction = instruction;
        this.from = from;
        this.to = to;
    }
}
