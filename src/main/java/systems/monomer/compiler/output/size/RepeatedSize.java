package systems.monomer.compiler.output.size;

import systems.monomer.compiler.output.CompileConstant;
import systems.monomer.compiler.output.CompileValue;

public class RepeatedSize extends CompileSize {
    private CompileSize unit;
    private CompileValue repetitions;

    public RepeatedSize(CompileSize unit, CompileValue repetitions) {
        this.unit = unit;
        this.repetitions = repetitions;
    }

    @Override
    public CompileSize simplify() {
        unit = unit.simplify();
        repetitions = repetitions.simplify();

        if(repetitions instanceof CompileConstant cc && unit instanceof FixedSize fs) {
            return new FixedSize(cc.coerceInt() * fs.getByteSize()).simplify();
        } else {
            return this;
        }
    }

    @Override
    public String toString() {
        return unit.toString() + "*" + repetitions.toString();
    }
}
