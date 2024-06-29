package systems.monomer.variables;

import systems.monomer.Constants;
import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.interpreter.InterpretNode;
import systems.monomer.interpreter.literals.InterpretStructureNode;
import systems.monomer.interpreter.literals.InterpretTupleNode;
import systems.monomer.interpreter.values.InterpretObject;
import systems.monomer.interpreter.values.InterpretTuple;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.controls.ReturnNode;
import systems.monomer.syntaxtree.literals.StructureNode;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.syntaxtree.operators.CallNode;
import systems.monomer.types.AnyType;
import systems.monomer.types.Signature;
import systems.monomer.types.TupleType;
import systems.monomer.types.Type;
import systems.monomer.util.Pair;
import systems.monomer.util.PairList;

import java.util.ArrayDeque;
import java.util.List;
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
    public ModuleNode getWrapper() {
        return parent;
    }
    public Node getBody() {
        return body;
    }

    private final ArrayDeque<Map<String, VariableKey>> recursiveSlices = new ArrayDeque<>();

    private boolean isLastStatement = false;    //TODO use this in call
    @Override
    public InterpretValue call(InterpretValue args, InterpretValue namedArgs) {
        assert namedArgs instanceof InterpretObject;

        if(recursiveSlices.size() > Constants.RECURSIVE_LIMIT) {
            throw new Error("Recursive limit exceeded (" + Constants.RECURSIVE_LIMIT + ")");
        }
        boolean optimized = this.args.size() == 0 || isLastStatement; //|| isTailRecursive();   //TODO decide if tail recursion is accounted for by isLastStatement
        if(optimized) parent.getVariables().clear();
        else recursiveSlices.push(parent.getVariableValuesMap());

        ((InterpretStructureNode) this.namedArgs).interpretValue();
        InterpretObject namedArgsObj = (InterpretObject)namedArgs;
        for(Map.Entry<String, Type> entry : namedArgsObj.getFields().entrySet()) {
            InterpretValue val = (InterpretValue) entry.getValue();
            this.namedArgs.getVariable(entry.getKey()).setValue(val);
        }

        InterpretTuple argsTuple = InterpretTuple.toTuple(args);
        //InterpretTuple paramTuple = new InterpretTuple(this.args.getChildren().stream().map(Node::interpretVariable).toList());
        ((InterpretTupleNode)this.args).interpretAssign(argsTuple, true);

        assert body instanceof InterpretNode;

        InterpretValue ret = ((InterpretNode) body).interpretValue().asValue();
        if(!optimized) parent.setVariableValues(recursiveSlices.pop());
        return ret;
    }

    boolean isTailRecursive = false; //cache
    boolean cached = false;
    private boolean isTailRecursive() {
        if (cached) return isTailRecursive;
        cached = true;

        isTailRecursive = true;

        PairList<List<Node>, Integer> stack = new PairList<>();
        List<Node> currentSiblings = body.getChildren();
        int currentIndex = 0;

        //TODO this is probably better done with recursion
        while(!(stack.isEmpty() && currentIndex >= currentSiblings.size())) {
            if(currentIndex >= currentSiblings.size()) {
                Pair<List<Node>, Integer> pair = stack.remove(stack.size() - 1);
                currentSiblings = pair.getFirst();
                currentIndex = pair.getSecond();
            }
            Node current = currentSiblings.get(currentIndex);
            if(current instanceof ReturnNode returnNode) {
                //if the return is this function and this functions' arguments do not include any concerning calls, then it's tail
                //TODO actually check
                isTailRecursive = false;
                break;
            } if(current instanceof CallNode call) {
                Node called = call.getFirst();
                //if called is done on this function or a run-time function or it calls this function in its body, then it's not tail
                //but if it's a simple utility function, then it can be tail
                //TODO actually check
                isTailRecursive = false;
                break;
            } else if(current.size() > 0) {
                stack.add(currentSiblings, currentIndex + 1);
                currentSiblings = current.getChildren();
                currentIndex = 0;
            } else {
                currentIndex++;
            }
        }

        return isTailRecursive;
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
