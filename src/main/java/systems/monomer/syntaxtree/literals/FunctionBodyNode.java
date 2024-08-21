package systems.monomer.syntaxtree.literals;

import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;

public class FunctionBodyNode extends ModuleNode {
    public FunctionBodyNode(Node args, Node namedArgs, Node body) {
        super("function");
        //it is necessary to set a type here for
        setType(null);
    }

    @Override
    public void matchVariables() {
        for (int i = 0; i < size(); i++) {
            get(i).matchVariables();
        }
    }

    @Override
    public void matchTypes() {
        for (int i = 0; i < size(); i++) {
            get(i).matchTypes();
        }
    }
}
