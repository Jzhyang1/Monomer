package systems.monomer.types;

import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretVariableNode;
import systems.monomer.syntaxtree.*;
import systems.monomer.variables.FunctionBody;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.literals.StructureNode;
import systems.monomer.variables.OverloadedFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static systems.monomer.compiler.Assembly.Operand.POINTER_SIZE;
import static systems.monomer.syntaxtree.Configuration.create;

@Getter
public class OverloadedFunctionType extends AnyType {
    private final List<Signature> overloads = new ArrayList<>();

    /**
     * gets the exact overload matching _signature_
     * @param signature
     * @return the exact overload or null
     */
    public @Nullable Signature getOverload(Signature signature) {
        //TODO forward references
        int index = randomAccessIndex(signature);
        return index == -1 ? null : overloads.get(index);
    }
    public void putInterpretOverload(FunctionBody signature) {
        overloads.add(signature);

        //These handle unknown return types and unknown argument types
//        if(signature.getReturnType() != ANY)
//            overloads.add(new Signature(ANY, signature.getArgs()), function);
//        if(signature.getArgs() != ANY)
//            overloads.add(new Signature(signature.getReturnType(), ANY), function);
//        if(signature.getReturnType() != AnyType.ANY && signature.getArgs() != AnyType.ANY)
//            overloads.put(new Signature(AnyType.ANY, AnyType.ANY), function);
    }

    public void putTypeOverload(List<? extends Type> args, Type ret) {
        overloads.add(new Signature(new TupleType(args), ret));
    }

    public void putInterpretOverload(Node args, StructureNode namedArgs, Node body, ModuleNode wrapper) {
        putInterpretOverload(new FunctionBody(args, namedArgs, body, wrapper));
    }

    public void putInterpretOverload(List<Type> argTypes, Function<List<VariableNode>, Node> bodyCallback) {
        List<VariableNode> args = IntStream.range(0, argTypes.size())
                .mapToObj(i ->
                        (VariableNode)create().variableNode("arg"+i).with(argTypes.get(i))
                )
                .collect(Collectors.toList());
        Node body = bodyCallback.apply(args);
        Node argsTuple = args.size() == 1 ? args.get(0) : create().tupleNode(args);

        ModuleNode wrapper = create().moduleNode("function");
        wrapper.with(argsTuple).with(body).matchVariables();
        wrapper.matchTypes();

        putInterpretOverload(argsTuple, StructureNode.EMPTY, body, wrapper);
    }

    public void putSingleInterpretOverload(Type argType, Type retType, Function<InterpretValue, InterpretResult> function) {
        InterpretVariableNode argVar = (InterpretVariableNode)create().variableNode("arg").with(argType);
        Node body = create().definedValueNode(
                ()->function.apply(argVar.interpretValue())
        ).with(retType);

        ModuleNode wrapper = create().moduleNode("function");
        wrapper.with(argVar).with(body).matchVariables();
        wrapper.matchTypes();

        putInterpretOverload(argVar, StructureNode.EMPTY, body, wrapper);
    }

    /**
     * returns the matching overload (ie _signature_ is contained by an existing signature)
     * @param signature
     * @return the matching overload
     */
    public @SneakyThrows Signature matchingOverload(Signature signature) {
        int index = randomAccessIndex(signature);

        if(index != -1) {
            return overloads.get(index);
        }
        else {
            throw new Error("No matching signature found for " + signature);  //TODO throwError
        }
    }

    public int randomAccessIndex(Signature signature) {
        int index = -1;
//        boolean conflictingMatches = false;
        for(int i = overloads.size() - 1; i >= 0; i--) {
            if(signature.equals(overloads.get(i))){
                index = i;
                break;
            }
            else if(signature.typeContains(overloads.get(i))) {
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
                && overloadedFunctionType.overloads.stream().allMatch(sig -> randomAccessIndex(sig) != -1);
    }

    public Signature getSignature(int randomAccessIndex) {
        return overloads.get(randomAccessIndex);
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
