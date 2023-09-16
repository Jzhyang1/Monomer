package systems.monomer.types;

import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;
import systems.monomer.interpreter.InterpretFunction;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.VariableNode;
import systems.monomer.syntaxtree.literals.TupleNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class OverloadedFunction extends AnyType {
    private final Map<Signature, InterpretFunction> overloads = new HashMap<>();    //TODO multikey map with Signature broken into returnType and args

    /**
     * gets the exact overload matching _signature_
     * @param signature
     * @return the exact overload or null
     */
    public @Nullable InterpretFunction getOverload(Signature signature) {
        //TODO forward references
        return overloads.get(signature);
    }
    public void putOverload(Signature signature, InterpretFunction function) {
        overloads.put(signature, function);

        //These handle unknown return types and unknown argument types
        if(signature.getReturnType() != ANY)
            overloads.put(new Signature(ANY, signature.getArgs()), function);
        if(signature.getArgs() != ANY)
            overloads.put(new Signature(signature.getReturnType(), ANY), function);
//        if(signature.getReturnType() != AnyType.ANY && signature.getArgs() != AnyType.ANY)
//            overloads.put(new Signature(AnyType.ANY, AnyType.ANY), function);
    }

    public void putOverload(Node args, Node body, ModuleNode wrapper) {
        putOverload(new Signature(body.getType(), args.getType()), new InterpretFunction(TupleNode.asTuple(args), body, wrapper));
    }

    public void putOverload(List<Type> argTypes, Function<List<VariableNode>, Node> bodyCallback) {
        List<VariableNode> args = IntStream.range(0, argTypes.size())
                .mapToObj(i -> {
                    VariableNode ret = new VariableNode("arg"+i);
                    ret.setType(argTypes.get(i));
                    return ret;
                }) //TODO type is wrong
                .collect(Collectors.toList());
        Node body = bodyCallback.apply(args);
        TupleNode argsTuple = new TupleNode(args);

        ModuleNode wrapper = new ModuleNode("function");
        wrapper.with(argsTuple).with(body).matchVariables();
        wrapper.matchTypes();

        putOverload(argsTuple, body, wrapper);
    }

    /**
     * returns the matching overload (ie _signature_ is contained by an existing signature)
     * @param signature
     * @return the matching overload
     */
    public @SneakyThrows InterpretFunction matchingOverload(Signature signature) {
        //TODO fix this (currently doesn't handle type-matching)
        if(overloads.containsKey(signature)) {
            return overloads.get(signature);
        }
        else {
            boolean needsRet = signature.getReturnType() != ANY;
            boolean needsArg = signature.getArgs() != ANY;
            Signature tempRetSignature = new Signature(ANY, signature.getArgs());
            Signature tempArgSignature = new Signature(signature.getReturnType(), ANY);

            if(needsRet && needsArg &&
                    overloads.containsKey(tempRetSignature) && overloads.containsKey(tempArgSignature)) {
                throw new Error("Ambiguous overload for " + signature);  //TODO throwError
            } else if(needsRet && overloads.containsKey(tempRetSignature)) {
                return overloads.get(tempRetSignature);
            } else if(needsArg && overloads.containsKey(tempArgSignature)) {
                return overloads.get(tempArgSignature);
            } else {
                throw new Error("No matching signature found for " + signature);  //TODO throwError
            }
        }
    }
}
