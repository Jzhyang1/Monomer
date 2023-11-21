package systems.monomer.compiler;

import lombok.Getter;
import systems.monomer.compiler.Assembly.Operand;

@Getter
public class CompileSize {
    private final Operand fixedSize;
    //TODO allow calculations to get the size

    public CompileSize(int fixedSize) {
        this.fixedSize = new Operand(Operand.Type.IMMEDIATE, null, 0, fixedSize);
    }
    public CompileSize(Operand fixedSize) {
        this.fixedSize = fixedSize;
    }

    public boolean isConstantSize() {
        return fixedSize.type == Operand.Type.IMMEDIATE;
    }

    /**
     * Returns the size of the object in bytes.
     * if the size is not fixed, throws an error.
     * @return the size of the object in bytes
     */
    public int getConstantSize() {
        if(fixedSize.type == Operand.Type.IMMEDIATE) {
            return fixedSize.immediate;
        }
        throw new Error("attempting to compile size of non-fixed size object");
    }

    /**
     * Returns the location of the int that stores the byte size.
     * @return the location of the int that stores the size.
     */
    public Operand getSizeLocation() {
        if(fixedSize.type == Operand.Type.MEMORY) {
            return fixedSize;
        }
        throw new Error("attempting to compile size of non-fixed size object");
    }

    /**
     * Returns the size of the value if it is fixed, otherwise returns the size of the size-pointer block that
     * is added to the stack.
     * @return the size required to store the value or reference
     */
    public int getOccupiedStackSize() {
        if(fixedSize.type == Operand.Type.IMMEDIATE) {
            return fixedSize.immediate;
        }
        return Operand.SIZE_POINTER_SIZE;
    }
}
