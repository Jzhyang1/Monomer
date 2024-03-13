package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.types.CollectionType;
import systems.monomer.types.SequenceType;
import systems.monomer.types.Type;

public class SpreadNode extends OperatorNode {
    public SpreadNode() {
        super("spread");
    }

    @Override
    public void matchTypes() {
        super.matchTypes();
        Type operandType = getFirst().getType();
        if(operandType instanceof CollectionType operandCollectionType)
            setType(new SequenceType(operandCollectionType.getElementType()));
        else
            throw syntaxError("Cannot spread non-collection type " + operandType);
    }

    @Override
    public InterpretResult interpretValue() {
        throw syntaxError("TODO unimplemented");    //TODO
    }

    @Override
    public Operand compileValue(AssemblyFile file) {
        throw syntaxError("TODO unimplemented");    //TODO
    }

    @Override
    public CompileSize compileSize() {
        throw syntaxError("TODO unimplemented");    //TODO
    }
}
