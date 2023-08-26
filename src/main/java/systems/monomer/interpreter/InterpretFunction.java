package systems.monomer.interpreter;

import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.syntaxtree.operators.AssignNode;
import systems.monomer.types.Signature;

public class InterpretFunction extends Signature implements InterpretValue {
    private TupleNode args;
    private Node body;

    //TODO handle named args
    public InterpretFunction(TupleNode args, Node body) {
        super(args.getType(), body.getType());
        this.args = args;
        this.body = body;
    }

    @Override
    //TODO handle named args
    public InterpretValue call(InterpretValue args) {
        InterpretTuple argsTuple = InterpretTuple.toTuple(args);
        InterpretTuple paramTuple = new InterpretTuple(this.args.getChildren().stream().map(Node::getVariableKey).toList());

        AssignNode.assign(paramTuple, argsTuple);

        return body.interpretValue();
    }
}
