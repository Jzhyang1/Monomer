package systems.monomer.types;

import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;
import systems.monomer.interpreter.InterpretFunction;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.VariableNode;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.util.PairList;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class OverloadedFunction extends AnyType {
//    private final Map<Signature, InterpretFunction> overloads = new HashMap<>();    //TODO multikey map with Signature broken into returnType and args
    private final PairList<Signature, InterpretFunction> overloads = new PairList<>();

    /**
     * gets the exact overload matching _signature_
     * @param signature
     * @return the exact overload or null
     */
    public @Nullable InterpretFunction getOverload(Signature signature) {
        //TODO forward references
        int index = randomAccessIndex(signature);
        return index == -1 ? null : overloads.get(index).getSecond();
    }
    public void putOverload(Signature signature, InterpretFunction function) {
        overloads.add(signature, function);

        //These handle unknown return types and unknown argument types
//        if(signature.getReturnType() != ANY)
//            overloads.add(new Signature(ANY, signature.getArgs()), function);
//        if(signature.getArgs() != ANY)
//            overloads.add(new Signature(signature.getReturnType(), ANY), function);
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
        Node argsTuple = args.size() == 1 ? args.get(0) : new TupleNode(args);

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
        int index = randomAccessIndex(signature);

        if(index != -1) {
            return overloads.get(index).getSecond();
            //TODO optimize by returning a randomAccessIndex or related key instead of the function
            //  where the function is referenced during type matching
        }
        else {
            throw new Error("No matching signature found for " + signature);  //TODO throwError
        }
    }

    public int randomAccessIndex(Signature signature) {
        int index = -1;
        for(int i = overloads.size() - 1; i >= 0; i--) {
            if(signature.equals(overloads.get(i).getFirst())){
                index = i;
                break;
            }
            else if(overloads.get(i).getFirst().typeContains(signature))
                index = i;
        }
        return index;
    }

    public InterpretFunction getFunction(int randomAccessIndex) {
        return overloads.get(randomAccessIndex).getSecond();
    }
}
