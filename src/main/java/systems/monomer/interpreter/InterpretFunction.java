package systems.monomer.interpreter;

import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.literals.TupleNode;

import java.util.List;

public class InterpretFunction extends InterpretValue {
    private TupleNode args;
    private Node body;

    //TODO handle args and named args
    public InterpretFunction(TupleNode args, Node body) {
        ModuleNode wrapper = new ModuleNode("function");
        wrapper.add(body);
        body.matchVariables();
        body.matchTypes();
        body.matchOverloads();
        this.body = body;
    }

    @Override
    //TODO add InterpretValue named args
    public InterpretValue call(InterpretValue args) {
        return body.interpretValue();
    }
}
