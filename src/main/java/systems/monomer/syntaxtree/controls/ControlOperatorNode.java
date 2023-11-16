package systems.monomer.syntaxtree.controls;

import org.jetbrains.annotations.Nullable;
import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretBool;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretTuple;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.operators.OperatorNode;
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

    private Map<String, VariableKey> variables = new HashMap<>();

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
        throwError("Control operator must appear in a control group. If you are getting this error, please report it as a bug.");
        return null;
    }

    public abstract InterpretControlResult interpretControl(boolean previousSuccess, boolean previousFailure, InterpretValue previousValue);
    protected InterpretControlResult interpretControl(Function<InterpretBool, InterpretControlResult> callback) {
        //TODO unchecked asValue
        InterpretValue condition = getFirst().interpretValue().asValue();
        if (condition instanceof InterpretBool boolCondition) {
            return callback.apply(boolCondition);
        }
        getFirst().throwError("Condition must be a boolean");
        return null;
    }

    @Override
    public Operand compileValue(AssemblyFile file) {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
