package systems.monomer.variables;

import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariableNode;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.VariableNode;
import systems.monomer.syntaxtree.literals.StructureNode;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.types.function.OverloadedFunctionType;
import systems.monomer.types.signature.Signature;
import systems.monomer.types.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OverloadedFunction extends OverloadedFunctionType implements InterpretValue {
    private final ArrayList<FunctionBody> overloads = new ArrayList<>();

    public OverloadedFunction(){}
    public OverloadedFunction(List<? extends Signature> overloads) {
        super(overloads);
    }

    public void putInterpretOverload(Node args, StructureNode namedArgs, Node body, ModuleNode wrapper) {
        putOverload(new FunctionBody(args, namedArgs, body, wrapper));
    }

    public void putInterpretOverload(List<Type> argTypes, Function<List<VariableNode>, Node> bodyCallback) {
        List<VariableNode> args = IntStream.range(0, argTypes.size())
                .mapToObj(i ->
                        (VariableNode) Node.init.variableNode("arg"+i).with(argTypes.get(i))
                )
                .collect(Collectors.toList());
        Node body = bodyCallback.apply(args);
        Node argsTuple;
        argsTuple = args.size() == 1 ? args.get(0) : Node.init.tupleNode(args);

        ModuleNode wrapper = Node.init.moduleNode("function");
        wrapper.with(argsTuple).with(body).matchVariables();
        wrapper.matchTypes();

        putInterpretOverload(argsTuple, StructureNode.EMPTY, body, wrapper);
    }

    public void putSingleInterpretOverload(Type argType, Type retType, Function<InterpretValue, InterpretResult> function) {
        InterpretVariableNode argVar = (InterpretVariableNode) Node.init.variableNode("arg").with(argType);
        Node body = Node.init.definedValueNode(
                ()->function.apply(argVar.interpretValue())
        ).with(retType);

        ModuleNode wrapper = Node.init.moduleNode("function");
        wrapper.with(argVar).with(body).matchVariables();
        wrapper.matchTypes();

        putInterpretOverload(argVar, StructureNode.EMPTY, body, wrapper);
    }

    public void putSupplierInterpretOverload(Type retType, Supplier<InterpretResult> function) {
        Node body = Node.init.definedValueNode(function).with(retType);

        ModuleNode wrapper = Node.init.moduleNode("function");
        wrapper.with(body).matchVariables();
        wrapper.matchTypes();

        putInterpretOverload(TupleNode.EMPTY, StructureNode.EMPTY, body, wrapper);
    }


    @Override
    public OverloadedFunction clone() {
        return (OverloadedFunction) super.clone();
    }

    @Override
    public String toString() {
        return "OverloadedFunction{" +
                "overloads=" + getOverloads() +
                '}';
    }
}
