package systems.monomer.syntaxtree.controls;

import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.operators.OperatorNode;
import systems.monomer.types.pseudo.AnyType;
import systems.monomer.types.Type;

public class ControlGroupNode extends OperatorNode {
    public ControlGroupNode(){
        super("control");
    }

    @Override
    public Usage getUsage() {
        return Usage.CONTROL_GROUP;
    }

    public Node matchTypes() {
        super.matchTypes();
        Type closestType = AnyType.ANY;
        for (int i = 0; i < size(); i++) {
            Type type = get(i).getType();
            if(closestType.typeContains(type)) {
                closestType = type;
            } else if (!type.typeContains(closestType)) {
                throw syntaxError("Types of branches do not match in control group (" + type + " vs " + closestType + ")");
            }
        }
        setType(closestType);
        return this;
    }

    public ControlOperatorNode get(int index) {
        return (ControlOperatorNode) super.get(index);
    }
    public ControlOperatorNode getFirst() {
        return (ControlOperatorNode) super.getFirst();
    }
    public ControlOperatorNode getSecond() {
        return (ControlOperatorNode) super.getSecond();
    }
}
