package systems.monomer.compiler.output;

import systems.monomer.compiler.output.size.CompileSize;

public class CompileRegister {
    public enum Register {
        A, B, C, D, E, SI, DI, SP, BP, R8, R9, R10, R11, R12, R13, R14, R15;
    }

    private final Register register;
    private final CompileSize size;
    public CompileRegister(Register register, CompileSize size) {
        this.register = register;
        this.size = size;
    }

    public String toString() {
        return this.register + "(" + size + ")";
//                switch (size.getByteSize()) {
//            case 1 -> "";
//            case 2 -> "H";
//            case 8 -> "X";
//            case 32 -> "EX";
//            default -> throw new IllegalStateException("Unexpected value: " + size.getByteSize());
//        };
    }
}
