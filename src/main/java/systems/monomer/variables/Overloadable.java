package systems.monomer.variables;

import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariableNode;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.literals.StructureNode;
import systems.monomer.types.Type;
import systems.monomer.types.function.OverloadableType;
import systems.monomer.types.signature.Signature;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static systems.monomer.errorhandling.ErrorBlock.programError;
import static systems.monomer.execution.Handler.init;

//TODO why is Overloadable a thing? shouldn't it all be OverloadedFunction?
public class Overloadable extends OverloadableType implements InterpretValue {
    private final ArrayList<FunctionBody> overloads = new ArrayList<>();

    public Overloadable(){}
    public Overloadable(List<? extends Signature> overloads) {
        super(overloads);
    }

    public void putInterpretOverload(Node args, StructureNode namedArgs, Node body, ModuleNode wrapper) {
        add(new FunctionBody(args, namedArgs, body, wrapper));
    }

    public void putSingleInterpretOverload(Type argType, Type retType, Function<InterpretValue, InterpretResult> function) {
        InterpretVariableNode argVar = (InterpretVariableNode) init.variableNode("arg").with(argType);
        Node body = init.definedValueNode(
                ()->function.apply(argVar.interpretValue())
        ).with(retType);

        ModuleNode wrapper = init.moduleNode("function");
        wrapper.with(argVar).with(body).matchVariables();
        wrapper.matchTypes();

        putInterpretOverload(argVar, init.emptyStructure(), body, wrapper);
    }

    public void putSupplierInterpretOverload(Type retType, Supplier<InterpretResult> function) {
        Node body = init.definedValueNode(function).with(retType);

        ModuleNode wrapper = init.moduleNode("function");
        wrapper.with(body).matchVariables();
        wrapper.matchTypes();

        putInterpretOverload(init.emptyTuple(), init.emptyStructure(), body, wrapper);
    }


    @Override
    public InterpretValue getField(String name) {
        throw programError("Can not acccess field in " + this, ErrorBlock.Reason.SYNTAX);
    }

    @Override
    public int compareValueTo(InterpretValue other) {
        //TODO
        return compareTo(other);
    }

    @Override
    public Overloadable clone() {
        try {
            return (Overloadable) super.clone();
        } catch (CloneNotSupportedException e) {
            throw programError("Unable to clone " + this, ErrorBlock.Reason.OTHER);
        }
    }

    @Override
    public String toString() {
        return "OverloadedFunction" + super.valueString();
    }
}
