package systems.monomer.compiler.assembly;

public enum Register {
    //x86 registers
    AX, BX, CX, DX, SI, DI, SP, BP,
    EAX, EBX, ECX, EDX, ESI, EDI, ESP, EBP,
    E8, E9, E10, E11, E12, E13, E14, E15,
    RAX, RBX, RCX, RDX, RSI, RDI, RBP, RSP,
    R8, R9, R10, R11, R12, R13, R14, R15;

    public Operand toOperand() {
        return new Operand(systems.monomer.compiler.assembly.Operand.Type.REGISTER, this, 0, 0);
    }
}