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

    public String toString() {
        return instruction + "\t" + (from == null ? "" : from) + "\t" + (to == null ? "" : to);
    }

    public boolean significant() {  //Optimization
        return !(
                (instruction == Instruction.MOV && from.equals(to))
                );
    }

    public String toAssembly() {
        return instruction.toAssembly() + "\t" + (from == null ? "" : from.toAssembly()) + "\t" + (to == null ? "" : to.toAssembly());
    }
}
