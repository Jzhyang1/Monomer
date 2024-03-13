package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretTuple;
import systems.monomer.types.Type;

public class WithThenNode extends OperatorNode {
    private final int valueIndex;
    public WithThenNode(String name) {
        super(name);
        valueIndex = "then".equals(name) ? 1 : 0;

        assert "with".equals(name) || "then".equals(name);
    }

    @Override
    public InterpretResult interpretValue() {
        InterpretResult result1 = getFirst().interpretValue();
        if(!result1.isValue()) return result1;

        InterpretResult result2 = getSecond().interpretValue();
        if(!result2.isValue()) return result2;

        if(!isThisExpression()) return InterpretTuple.EMPTY;
        return checkedResult(valueIndex == 0 ? result1 : result2);
    }

    @Override
    public void matchTypes() {
        super.matchTypes();
        setType(get(valueIndex).getType());
    }

    @Override
    public void setIsExpression(boolean isExpression) {
        super.setIsExpression(isExpression);
    }

    public Operand compileValue(AssemblyFile file) {
        throw new Error("TODO unimplemented");
    }

    @Override
    public CompileSize compileSize() {
        return null;
    }
}
