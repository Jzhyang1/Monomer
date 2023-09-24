package systems.monomer.interpreter;

import systems.monomer.Constants;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.StructureNode;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.syntaxtree.operators.AssignNode;
import systems.monomer.types.AnyType;
import systems.monomer.types.Signature;
import systems.monomer.types.TupleType;
import systems.monomer.types.Type;
import systems.monomer.variables.VariableKey;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;
import java.util.stream.IntStream;

public class InterpretFunction extends Signature implements InterpretValue {
    private TupleNode args;
    private StructureNode namedArgs;
    private Node body;
    private final ModuleNode parent;

    //TODO handle named args
    public InterpretFunction(Node args, StructureNode namedArgs, Node body, ModuleNode parent) {
        super(null, null);
        this.args = TupleNode.asTuple(args);
        this.namedArgs = namedArgs;
        this.body = body;
        this.parent = parent;
    }

    @Override
    public Type getReturnType() {
        Type t = body.getType();
        return t == null ? AnyType.ANY : t;
    }

    @Override
    public Type getArgs() {
        Type t = args.getType();
        return t == null ? AnyType.ANY : t;
    }

    private final ArrayDeque<Map<String, VariableKey>> recursiveSlices = new ArrayDeque<>();
    @Override
    //TODO handle named args
    public InterpretValue call(InterpretValue args) {
        if(recursiveSlices.size() > Constants.RECURSIVE_LIMIT) {
            throw new Error("Recursive limit exceeded (" + Constants.RECURSIVE_LIMIT + ")");
        }
        //TODO optimize to not push if args is empty or if tail recursion
        recursiveSlices.push(parent.getVariableValuesMap());

        InterpretTuple argsTuple = InterpretTuple.toTuple(args);
        //InterpretTuple paramTuple = new InterpretTuple(this.args.getChildren().stream().map(Node::interpretVariable).toList());

        AssignNode.assign(this.args, argsTuple);

        //TODO unchecked asValue
        InterpretValue ret = body.interpretValue().asValue();
        parent.setVariableValues(recursiveSlices.pop());
        return ret;
    }

    private boolean isTesting = false;
    public Type testReturnType(Type argType) {
        if(isTesting) return AnyType.ANY;
        else isTesting = true;

        TupleType argTypes = TupleType.asTuple(argType);
        IntStream.range(0, args.size()).forEach((i)->args.get(i).getVariableKey().setType(argTypes.getType(i)));
        args.matchTypes();
        body.matchTypes();

        isTesting = false;
        return body.getType();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof InterpretFunction function &&
                getReturnType().equals(function.getReturnType()) &&
                getArgs().equals(function.getArgs());
    }

    @Override
    public int hashCode() {
        return getReturnType().hashCode() + getArgs().hashCode() * 31 + this.getClass().hashCode() * 31 * 31;
    }

    @Override
    public InterpretFunction clone() {
        throw new Error("TODO unimplemented");
    }

    @Override
    public String toString() {
        return getArgs().valueString() + " -> " + getReturnType().valueString();
    }
}
