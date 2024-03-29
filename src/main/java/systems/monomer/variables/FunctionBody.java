package systems.monomer.variables;

import systems.monomer.Constants;
import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.interpreter.InterpretObject;
import systems.monomer.interpreter.InterpretTuple;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.literals.StructureNode;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.types.AnyType;
import systems.monomer.types.Signature;
import systems.monomer.types.TupleType;
import systems.monomer.types.Type;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.stream.IntStream;

public class FunctionBody extends Signature implements InterpretValue {
    private final TupleNode args;
    private final StructureNode namedArgs;
    private final Node body;
    /**
     * where the variables are stored
     */
    private final ModuleNode parent;

    public FunctionBody(Node args, StructureNode namedArgs, Node body, ModuleNode parent) {
        super(null, null);
        this.args = TupleNode.asTuple(args);
        this.namedArgs = namedArgs;
        this.body = body;
        this.parent = parent;
    }

//    public FunctionBody(List<Type> argTypes, Function<List<VariableNode>, Node> bodyCallback) {
//        super(null, null);
//
//        List<VariableNode> args = IntStream.range(0, argTypes.size())
//                .mapToObj(i -> {
//                    VariableNode ret = new VariableNode("arg"+i);
//                    ret.setType(argTypes.get(i));
//                    return ret;
//                })
//                .collect(Collectors.toList());
//        Node body = bodyCallback.apply(args);
//        Node argsTuple = args.size() == 1 ? args.get(0) : new TupleNode(args);
//
//        ModuleNode wrapper = new ModuleNode("function");
//        wrapper.with(argsTuple).with(body).matchVariables();
//        wrapper.matchTypes();
//
//        this.args = new TupleNode(args);
//        this.namedArgs = StructureNode.EMPTY;
//        this.body = body;
//        this.parent = wrapper;
//    }

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

    //TODO remove these methods somehow
    public TupleNode getArgNodes() {
        return args;
    }
    public StructureNode getNamedArgNodes() {
        return namedArgs;
    }
    public ModuleNode getWrapper() {
        return parent;
    }
    public Node getBody() {
        return body;
    }

    private final ArrayDeque<Map<String, VariableKey>> recursiveSlices = new ArrayDeque<>();

    @Override
    public InterpretValue call(InterpretValue args, InterpretValue namedArgs) {
        assert namedArgs instanceof InterpretObject;

        if(recursiveSlices.size() > Constants.RECURSIVE_LIMIT) {
            throw new Error("Recursive limit exceeded (" + Constants.RECURSIVE_LIMIT + ")");
        }
        //TODO optimize to not push if args is empty or if tail recursion
        recursiveSlices.push(parent.getVariableValuesMap());

        this.namedArgs.interpretValue();    //TODO optimize to not have to interpret everything
        InterpretObject namedArgsObj = (InterpretObject)namedArgs;
        for(Map.Entry<String, Type> entry : namedArgsObj.getFields().entrySet()) {
            InterpretValue val = (InterpretValue) entry.getValue();
            this.namedArgs.getVariable(entry.getKey()).setValue(val);
        }

        InterpretTuple argsTuple = InterpretTuple.toTuple(args);
        //InterpretTuple paramTuple = new InterpretTuple(this.args.getChildren().stream().map(Node::interpretVariable).toList());
        this.args.interpretAssign(argsTuple);

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

    public Operand compileValue(AssemblyFile file) {
        return null;    //TODO comple function and return label
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof FunctionBody function &&
                getReturnType().equals(function.getReturnType()) &&
                getArgs().equals(function.getArgs());
    }

    @Override
    public int hashCode() {
        return getReturnType().hashCode() + getArgs().hashCode() * 31 + this.getClass().hashCode() * 31 * 31;
    }

    @Override
    public InterpretValue defaultValue() {
        return this;
    }

    @Override
    public FunctionBody clone() {
        throw new Error("TODO unimplemented");
    }

    @Override
    public String toString() {
        return getArgs().valueString() + " -> " + getReturnType().valueString();
    }
}
