package systems.monomer.syntaxtree.controls;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.errorhandling.UnimplementedError;
import systems.monomer.interpreter.InterpretBreaking;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretTuple;
import systems.monomer.syntaxtree.TypeContext;
import systems.monomer.syntaxtree.operators.OperatorNode;
import systems.monomer.types.Type;

public class ReturnNode extends OperatorNode {
    public ReturnNode() {
        super("return");
    }


    @Override
    public InterpretResult interpretValue() {
        return new InterpretBreaking("return",
                size() == 0 ? InterpretTuple.EMPTY : getFirst().interpretValue().asValue()
        );
    }

    @Override
    public Type testType(TypeContext context) {
        Type ret = size() == 0 ? null : getFirst().testType(context);
        context.setReturnType(ret);
        return ret;
    }

    @Override
    public Operand compileValue(AssemblyFile file) {
        throw new Error("TODO unimplemented");
    }

    @Override
    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
