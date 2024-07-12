package systems.monomer.compiler.assembly;

/**
 * Represents an operand in an assembly instruction operand.
 *
 * Combinations:
 * type REGISTER, register
 * type MEMORY, register, offset
 * type MEMORY, immediate (for globals)
 * type IMMEDIATE, immediate
 *
 * use 0 or null for unused values
 */
public class Operand {
    /**
     * the number of bytes in a SIZE variable
     */
    public static final int SIZE_SIZE = 8;
    /**
     * the number of bytes in a POINTER variable
     */
    public static final int POINTER_SIZE = 8;
    /**
     * the number of bytes in a SIZE and a POINTER variable
     */
    public static final int SIZE_POINTER_SIZE = SIZE_SIZE + POINTER_SIZE;

    public enum Type {
        REGISTER, MEMORY, IMMEDIATE,
        /**
         * this is pseudo-type that represents an operand given
         * by a size and a pointer to a dynamic variable
         */
        MEMORY_OF_POINTER
    }

    public final Type type;

    public final Register register;
    public final int offset;
    public final int immediate;

    public Operand(int immediate) {
        this(Type.IMMEDIATE, null, 0, immediate);
    }
    public Operand(Type type, Register register, int offset, int immediate) {
        this.type = type;
        this.register = register;
        this.offset = offset;
        this.immediate = immediate;
    }

    public String toString() {
        switch(type) {
            case REGISTER:
                return register.toString();
            case MEMORY:
                if(offset == 0)
                    return "[" + register.toString() + "]";
                else
                    return "[" + register.toString() + "+" + offset + "]";
            case IMMEDIATE:
                return immediate > 128 ? "0x" + Integer.toHexString(immediate) : Integer.toString(immediate);
            case MEMORY_OF_POINTER:
                return "[" + register.toString() + "+" + offset + "]";
            default:
                throw new Error("TODO unimplemented");
        }
    }

    public String toAssembly() {
        return toString();  //TODO create a platform-specific map of formats
    }

    public boolean equals(Operand to) {
        return type == to.type &&
            register == to.register &&
            offset == to.offset &&
            immediate == to.immediate;
    }
}
