package systems.monomer.syntaxtree.controls;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretBool;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretTuple;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.operators.OperatorNode;
import systems.monomer.types.BoolType;
import systems.monomer.variables.VariableKey;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class ControlOperatorNode extends OperatorNode {
    public class InterpretControlResult {
        public final boolean isSuccess;
        public final boolean isBroken;
        public final InterpretResult value;

        public InterpretControlResult(boolean isSuccess, InterpretResult value) {
            this.isSuccess = isSuccess;
            this.isBroken = !value.isValue();
            this.value = isThisExpression() || isBroken ? value : InterpretTuple.EMPTY;
        }
    }

    private final Map<String, VariableKey> variables = new HashMap<>();

    protected ControlOperatorNode(String name){
        super(name);
    }

    public Usage getUsage() {
        return Usage.LABEL;
    }

    public void putVariable(String varName, VariableKey key) {
        variables.put(varName, key);
    }
    public VariableKey getVariable(String varName) {
        return variables.containsKey(varName) ? variables.get(varName) : getParent().getVariable(varName);
    }

    public void matchTypes() {
        super.matchTypes();
        setType(getSecond().getType());
    }

    public InterpretValue interpretValue() {
        syntaxError("Control operator must appear in a control group. If you are getting this error, please report it as a bug.");
        return null;
    }

    public abstract InterpretControlResult interpretControl(boolean previousSuccess, boolean previousFailure, InterpretValue previousValue);
    protected InterpretControlResult interpretControl(Function<Boolean, InterpretControlResult> callback) {
        //TODO unchecked asValue
        InterpretValue condition = getFirst().interpretValue().asValue();
        if (BoolType.BOOL.typeContains(condition)) {
            return callback.apply(condition.<Boolean>getValue());
        }
        throw getFirst().syntaxError("Condition must be a boolean, got " + condition.getType());
    }

    @Override
    public Operand compileValue(AssemblyFile file) {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
