package systems.monomer.syntaxtree.controls;

import systems.monomer.syntaxtree.TypeContext;
import systems.monomer.syntaxtree.operators.OperatorNode;
import systems.monomer.types.Type;

public class ReturnNode extends OperatorNode {
    public ReturnNode() {
        super("return");
    }

    @Override
    public Type testType(TypeContext context) {
        Type ret = size() == 0 ? null : getFirst().testType(context);
        context.setReturnType(ret);
        return ret;
    }
}
