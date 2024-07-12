package systems.monomer.compiler.assembly;

import systems.monomer.execution.Constants;

public enum Instruction {
    //x86 instructions
    MOV, LEA,
    PUSH, POP,
    IADD("ADD"), ISUB("SUB"), IMUL("MUL"), IDIV("DIV"), IMOD("FPREM"), IINC, IDEC, INEG,
    FADD("ADDSD"), FSUB("SUBSD"), FMUL("MULSD"), FDIV("DIVSD"), FMOD("FPREM"), FINC, FDEC, FNEG,
    AND, NAND("ANDN"), OR, XOR, NOR, XNOR, NOT, SHL, SHR,

    CMP, JMP, JE, JNE, JG, JGE, JL, JLE,

    CALL, RET,
    END,

    SYSCALL,

    //Pseudo instructions
    GLOBAL, DATA, TEXT, LABEL
    ;

    private final String win, unix;
    private Instruction(){
        this.win =  this.unix = this.toString().toLowerCase();}
    private Instruction(String both) {
        this.win = both;
        this.unix = both;
    }
    private Instruction(String win, String unix) {
        this.win = win;
        this.unix = unix;
    }

    public String toAssembly() {
        return Constants.IS_WINDOWS ? win : unix;
    }
}