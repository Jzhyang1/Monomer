package systems.monomer.syntaxtree.controls;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretBool;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.operators.OperatorNode;
import systems.monomer.variables.VariableKey;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class ControlOperatorNode extends OperatorNode {
    public static class InterpretControlResult {
        public final boolean isSuccess;
        public final InterpretValue value;

        public InterpretControlResult(boolean isSuccess, InterpretValue value) {
            this.isSuccess = isSuccess;
            this.value = value;
        }
    }

    private Map<String, VariableKey> variables = new HashMap<>();

    public ControlOperatorNode(String name){
        super(name);
    }

    public Usage getUsage() {
        return Usage.LABEL;
    }

    public void putVariable(String varName, VariableKey key) {
        variables.put(varName, key);
    }
    public VariableKey getVariable(String varName) {
        if(!variables.containsKey(varName)) return getParent().getVariable(varName);
        return variables.get(varName);
    }

    public void matchTypes() {
        super.matchTypes();
        setType(getSecond().getType());
    }

    public InterpretValue interpretValue() {
        throwError("Control operator must appear in a control group. If you are getting this error, please report it as a bug.");
        return null;
    }

    public abstract InterpretControlResult interpretControl(boolean previousSuccess, boolean previousFailure, InterpretValue previousValue);
    protected InterpretControlResult interpretControl(Function<InterpretBool, InterpretControlResult> callback) {
        InterpretValue condition = getFirst().interpretValue();
        if (condition instanceof InterpretBool boolCondition) {
            return callback.apply(boolCondition);
        }
        getFirst().throwError("Condition must be a boolean");
        return null;
    }

    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
