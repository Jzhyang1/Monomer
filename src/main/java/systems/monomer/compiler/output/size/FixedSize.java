package systems.monomer.compiler.output.size;

import lombok.Getter;

@Getter
public class FixedSize extends CompileSize {
    private final int byteSize;

    public FixedSize(int byteSize) {
        this.byteSize = byteSize;
    }

    @Override
    public CompileSize simplify() {
        //if a power of 2
        if ((byteSize & -byteSize) == byteSize) return SystemSize.getSystemSize(byteSize);
        else return this;
    }

    @Override
    public String toString() {
        return String.valueOf(byteSize);
    }
}
