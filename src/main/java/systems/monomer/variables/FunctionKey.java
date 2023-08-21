package systems.monomer.variables;

import systems.monomer.interpreter.InterpretFunction;
import systems.monomer.types.Signature;
import systems.monomer.types.Type;

import java.util.HashMap;
import java.util.Map;

public class FunctionKey extends VariableKey {
    private Map<Type, InterpretFunction> overloads = new HashMap<>();

    public InterpretFunction getOverload(Type type) {
        return overloads.get(type);
    }
    public void putOverload(Signature type, InterpretFunction function) {
        overloads.put(type, function);
    }
}
