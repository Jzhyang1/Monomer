package systems.monomer.types;

import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;
import systems.monomer.interpreter.InterpretFunction;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.literals.StructureNode;
import systems.monomer.syntaxtree.VariableNode;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.util.Pair;
import systems.monomer.util.PairList;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
public class OverloadedFunction extends AnyType {
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

    public void putOverload(Node args, StructureNode namedArgs, Node body, ModuleNode wrapper) {
        putOverload(new Signature(body.getType(), args.getType(), namedArgs.getType()), new InterpretFunction(args, namedArgs, body, wrapper));
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

        putOverload(argsTuple, StructureNode.EMPTY, body, wrapper);
    }

    public void putOverload(List<Type> argTypes,
                            List<Pair<String, Type>> names,
                            BiFunction<List<VariableNode>, List<VariableNode>, Node> bodyCallback) {
        List<VariableNode> args = IntStream.range(0, argTypes.size())
                .mapToObj(i -> {
                    VariableNode ret = new VariableNode("arg"+i);
                    ret.setType(argTypes.get(i));
                    return ret;
                })
                .toList();
        List<VariableNode> namedArgs = names.stream()
                .map(pair -> {
                    VariableNode ret = new VariableNode(pair.getFirst());
                    ret.setType(pair.getSecond());
                    return ret;
                })
                .toList();
        Node body = bodyCallback.apply(args, namedArgs);
        Node argsTuple = args.size() == 1 ? args.get(0) : new TupleNode(args);
        StructureNode namedArgsStructure = new StructureNode(); namedArgsStructure.addAll(namedArgs);

        ModuleNode wrapper = new ModuleNode("function");
        for(String fieldName : namedArgsStructure.getFieldNames())
            wrapper.putVariable(fieldName, namedArgsStructure.getVariable(fieldName));
        wrapper.with(argsTuple).with(namedArgsStructure).with(body).matchVariables();
        wrapper.matchTypes();

        putOverload(argsTuple, namedArgsStructure, body, wrapper);
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
        }
        else {
            throw new Error("No matching signature found for " + signature);  //TODO throwError
        }
    }

    public int randomAccessIndex(Signature signature) {
        int index = -1;
//        boolean conflictingMatches = false;
        for(int i = overloads.size() - 1; i >= 0; i--) {
            if(signature.equals(overloads.get(i).getFirst())){
                index = i;
                break;
            }
            else if(signature.typeContains(overloads.get(i).getFirst())) {
                if (index == -1) index = i;
//                else conflictingMatches = true;
            }
//            else if(overloads.get(i).getFirst().typeContains(signature)) {
//                if (index == -1) index = i;
////                else conflictingMatches = true;
//            }
        }
//        if(conflictingMatches) {  //TODO handle matching conflicts
//            throw new Error("Conflicting matches found for " + signature);  //TODO throwError
//        }
        return index;
    }

    public InterpretFunction getFunction(int randomAccessIndex) {
        return overloads.get(randomAccessIndex).getSecond();
    }
}
