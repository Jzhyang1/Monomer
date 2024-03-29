package systems.monomer.compiler;

import lombok.Getter;
import lombok.Setter;
import systems.monomer.compiler.Assembly.Instruction;
import systems.monomer.compiler.Assembly.Operand;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class AssemblyFile {
    @Getter
    private final List<AssemblyLine> lines = new ArrayList<>();
    /**
     * The lowest stack position used by the current function
     * with reference to the base pointer (EBP).
     * This will be a positive value, but will be used as a negative offset
     */
    @Getter
    @Setter
    private int lowestStackPosition = 0;

    public AssemblyFile add(Instruction instruction, Operand from, Operand to) {
        lines.add(new AssemblyLine(instruction, from, to));
        return this;
    }

    public AssemblyFile push(Operand from) {
        return add(Instruction.PUSH, from, null);
    }

    public AssemblyFile pop(Operand to) {
        return add(Instruction.POP, null, to);
    }

    public AssemblyFile mov(Operand from, Operand to) {
        if(from == to) return this;
        return add(Instruction.MOV, from, to);
    }

    public void addAll(List<AssemblyLine> lines) {
        this.lines.addAll(lines);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (AssemblyLine line : lines) {
            builder.append(line.toString());
            builder.append("\n");
        }
        return builder.toString();
    }

    public int incrementStackPosition(int amount) {
        lowestStackPosition += amount;
        return lowestStackPosition;
    }

    public int clearStackPosition() {
        lowestStackPosition = 0;
        return 0;
    }

    public void writeAssembly(Writer outputFile) throws IOException {
        outputFile.write("section .text\n");
        outputFile.write("global start\n");
        //variables

        outputFile.write("start:\n");
        outputFile.write("\tmov eax, 1\n");

        for (AssemblyLine line : lines) {
            if(!line.significant()) continue;

            outputFile.write("\t");
            outputFile.write(line.toAssembly());
            outputFile.write("\n");
        }
    }
    public void writeBinary(Writer outputFile) throws IOException {
        for (AssemblyLine line : lines) {
            //TODO
        }
    }

}
