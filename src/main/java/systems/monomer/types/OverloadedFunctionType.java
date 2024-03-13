package systems.monomer.types;

import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;
import systems.monomer.compiler.CompileSize;
import systems.monomer.variables.FunctionBody;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.literals.StructureNode;
import systems.monomer.syntaxtree.VariableNode;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.util.Pair;
import systems.monomer.util.PairList;
import systems.monomer.variables.OverloadedFunction;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static systems.monomer.compiler.Assembly.Operand.POINTER_SIZE;

@Getter
public class OverloadedFunctionType extends AnyType {
    private final PairList<Signature, FunctionBody> overloads = new PairList<>();

    /**
     * gets the exact overload matching _signature_
     * @param signature
     * @return the exact overload or null
     */
    public @Nullable FunctionBody getOverload(Signature signature) {
        //TODO forward references
        int index = randomAccessIndex(signature);
        return index == -1 ? null : overloads.get(index).getSecond();
    }
    public void putSystemOverload(Signature signature, FunctionBody function) {
        overloads.add(signature, function);

        //These handle unknown return types and unknown argument types
//        if(signature.getReturnType() != ANY)
//            overloads.add(new Signature(ANY, signature.getArgs()), function);
//        if(signature.getArgs() != ANY)
//            overloads.add(new Signature(signature.getReturnType(), ANY), function);
//        if(signature.getReturnType() != AnyType.ANY && signature.getArgs() != AnyType.ANY)
//            overloads.put(new Signature(AnyType.ANY, AnyType.ANY), function);
    }

    public void putSystemOverload(Node args, StructureNode namedArgs, Node body, ModuleNode wrapper) {
        putSystemOverload(new Signature(body.getType(), args.getType(), namedArgs.getType()), new FunctionBody(args, namedArgs, body, wrapper));
    }

    public void putSystemOverload(List<Type> argTypes, Function<List<VariableNode>, Node> bodyCallback) {
        List<VariableNode> args = IntStream.range(0, argTypes.size())
                .mapToObj(i -> {
                    VariableNode ret = new VariableNode("arg"+i);
                    ret.setType(argTypes.get(i));
                    return ret;
                })
                .collect(Collectors.toList());
        Node body = bodyCallback.apply(args);
        Node argsTuple = args.size() == 1 ? args.get(0) : new TupleNode(args);

        ModuleNode wrapper = new ModuleNode("function");
        wrapper.with(argsTuple).with(body).matchVariables();
        wrapper.matchTypes();

        putSystemOverload(argsTuple, StructureNode.EMPTY, body, wrapper);
    }

    public void putSystemOverload(List<Type> argTypes,
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

        putSystemOverload(argsTuple, namedArgsStructure, body, wrapper);
    }

    /**
     * returns the matching overload (ie _signature_ is contained by an existing signature)
     * @param signature
     * @return the matching overload
     */
    public @SneakyThrows FunctionBody matchingOverload(Signature signature) {
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

    @Override
    public boolean typeContains(Type type) {
        return type instanceof OverloadedFunctionType overloadedFunctionType
                && overloadedFunctionType.overloads.stream().allMatch(pair -> randomAccessIndex(pair.getFirst()) != -1);
    }

    public FunctionBody getFunction(int randomAccessIndex) {
        return overloads.get(randomAccessIndex).getSecond();
    }

    @Override
    public CompileSize compileSize() {
        return new CompileSize(POINTER_SIZE);
    }

    @Override
    public InterpretValue defaultValue() {
        return new OverloadedFunction(overloads);
    }
}
