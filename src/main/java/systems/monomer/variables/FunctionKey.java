package systems.monomer.variables;

import systems.monomer.interpreter.InterpretFile;
import systems.monomer.interpreter.InterpretFunction;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.VariableNode;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.types.AnyType;
import systems.monomer.types.NumberType;
import systems.monomer.types.Signature;
import systems.monomer.types.Type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FunctionKey implements InterpretValue {
    private Map<Signature, InterpretFunction> overloads = new HashMap<>();

    public InterpretFunction getOverload(Type type) {
        return overloads.get(type);
    }
    public void putOverload(Signature type, InterpretFunction function) {
        overloads.put(type, function);
    }

    public void putOverload(Node args, Node body, ModuleNode wrapper) {
        putOverload(new Signature(args.getType(), body.getType()), new InterpretFunction(TupleNode.asTuple(args), body, wrapper));
    }

    public void putOverload(List<Type> argTypes, Function<List<VariableNode>, Node> bodyCallback) {

        List<VariableNode> args = IntStream.range(0, argTypes.size())
                .mapToObj(i -> (VariableNode)(new VariableNode("arg"+i)).with(argTypes.get(i))) //TODO type is wrong
                .collect(Collectors.toList());
        Node body = bodyCallback.apply(args);
        TupleNode argsTuple = new TupleNode(args);

        ModuleNode wrapper = new ModuleNode("function");
        wrapper.with(argsTuple).with(body).matchVariables();

        putOverload(argsTuple, body, wrapper);
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

    @Override
    public FunctionKey clone() {
        try {
            return (FunctionKey) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String valueString() {
        return "function";
    }

    @Override
    public boolean typeContains(Type type) {
        throw new Error("TODO unimplemented");
    }

    @Override
    public boolean equals(Object other) {
        throw new Error("TODO unimplemented");
    }
}
