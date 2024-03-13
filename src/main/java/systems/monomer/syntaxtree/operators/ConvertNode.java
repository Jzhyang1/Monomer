package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.Node;

/**
 * ConvertNode is a node that represents a conversion operation
 * which differs from casting in that it creates a copy of the
 * value to have a different type.
 * The children of ConvertNode are:
 * <ol>
 *     <li> The value to be converted </li>
 *     <li> A value of the type to convert to </li>
 * </ol>
 */
public class ConvertNode extends OperatorNode {

    public ConvertNode() {
        super("to");
    }

    public void matchTypes() {
        super.matchTypes();
        setType(getSecond().getType());
        getFirst().setType(getType());
    }

    public InterpretResult interpretValue() {
        throw new Error("TODO unimplemented");
    }

    public Operand compileValue(AssemblyFile file) {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
