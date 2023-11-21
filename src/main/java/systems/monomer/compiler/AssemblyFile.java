package systems.monomer.compiler;

import lombok.Getter;
import systems.monomer.compiler.Assembly.Instruction;
import systems.monomer.compiler.Assembly.Operand;

import java.util.ArrayList;
import java.util.List;

public class AssemblyFile {
    @Getter
    private final List<AssemblyLine> lines = new ArrayList<>();


    public AssemblyFile add(Instruction instruction, Operand from, Operand to) {
        lines.add(new AssemblyLine(instruction, from, to));
        return this;
    }
    public AssemblyFile add(AssemblyLine line) {
        lines.add(line);
        return this;
    }
    public void addAll(List<AssemblyLine> lines) {
        this.lines.addAll(lines);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(AssemblyLine line : lines) {
            builder.append(line.toString());
            builder.append("\n");
        }
        return builder.toString();
    }
}
