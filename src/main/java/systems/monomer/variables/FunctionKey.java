package systems.monomer.variables;

import systems.monomer.interpreter.InterpretFunction;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.types.AnyType;
import systems.monomer.types.Signature;
import systems.monomer.types.Type;

import java.util.HashMap;
import java.util.Map;

public class FunctionKey extends VariableKey {
    private Map<Signature, InterpretFunction> overloads = new HashMap<>();

    public InterpretFunction getOverload(Type type) {
        return overloads.get(type);
    }
    public void putOverload(Signature type, InterpretFunction function) {
        overloads.put(type, function);
    }

    public void putOverload(Node args, Node body) {
        putOverload(new Signature(args.getType(), body.getType()), new InterpretFunction(TupleNode.asTuple(args), body));
    }

    @Override
    public InterpretValue call(InterpretValue args) {
        //TODO fix this (currently doesn't handle type-matching)
        if(overloads.containsKey(new Signature(AnyType.ANY, args))) {
            return overloads.get(args).call(args);
        } else if(overloads.size() == 1) {
            return overloads.values().iterator().next().call(args);
        } else {
            throw new Error("TODO unimplemented");  //TODO throwError
        }
    }
}
