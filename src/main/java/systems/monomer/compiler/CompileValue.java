package systems.monomer.compiler;

import lombok.Getter;
import systems.monomer.compiler.Assembly.Instruction;
import systems.monomer.compiler.Assembly.Operand;

import java.util.ArrayList;
import java.util.List;

public class CompileValue {
    @Getter
    private final List<AssemblyLine> lines = new ArrayList<>();


    public void add(Instruction instruction, Operand from, Operand to) {
        lines.add(new AssemblyLine(instruction, from, to));
    }
    public void add(AssemblyLine line) {
        lines.add(line);
    }
    public void addAll(List<AssemblyLine> lines) {
        this.lines.addAll(lines);
    }
}
