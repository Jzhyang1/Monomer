package systems.monomer.interpreter;

import systems.monomer.Constants;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.syntaxtree.operators.AssignNode;
import systems.monomer.types.Signature;
import systems.monomer.variables.VariableKey;

import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

public class InterpretFunction extends Signature implements InterpretValue {
    private TupleNode args;
    private Node body;
    private ModuleNode parent;

    //TODO handle named args
    public InterpretFunction(TupleNode args, Node body, ModuleNode parent) {
        super(args.getType(), body.getType());
        this.args = args;
        this.body = body;
        this.parent = parent;
    }


    private Stack<Map<String, VariableKey>> recursiveSlices = new Stack<>();
    @Override
    //TODO handle named args
    public InterpretValue call(InterpretValue args) {
        if(recursiveSlices.size() > Constants.RECURSIVE_LIMIT) {
            throw new Error("Recursive limit exceeded (" + Constants.RECURSIVE_LIMIT + ")");
        }
        //TODO optimize to not push if args is empty or if tail recursion
        recursiveSlices.push(parent.getVariableValuesMap());

        InterpretTuple argsTuple = InterpretTuple.toTuple(args);
        InterpretTuple paramTuple = new InterpretTuple(this.args.getChildren().stream().map(Node::getVariableKey).toList());

        AssignNode.assign(paramTuple, argsTuple);

        InterpretValue ret = body.interpretValue();
        parent.setVariableValues(recursiveSlices.pop());
        return ret;
    }
}
