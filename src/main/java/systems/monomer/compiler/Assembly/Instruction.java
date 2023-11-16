package systems.monomer.compiler.Assembly;

public enum Instruction {
    //x86 instructions
    MOV, LEA,
    PUSH, POP,
    IADD, ISUB, IMUL, IDIV, IMOD, IINC, IDEC, INEG,
    FADD, FSUB, FMUL, FDIV, FMOD, FINC, FDEC, FNEG,
    AND, OR, XOR, NOT, SHL, SHR,

    CMP, JMP, JE, JNE, JG, JGE, JL, JLE,

    CALL, RET,
}