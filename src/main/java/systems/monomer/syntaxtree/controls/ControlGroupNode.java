package systems.monomer.syntaxtree.controls;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretTuple;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.operators.OperatorNode;
import systems.monomer.types.Type;

public final class ControlGroupNode extends OperatorNode {
    public ControlGroupNode(){
        super("control");
    }

    @Override
    public Usage getUsage() {
        return Usage.CONTROL_GROUP;
    }

    public void matchTypes() {
        super.matchTypes();
        Type closestType = getFirst().getType();
        for (int i = 1; i < size(); i++) {
            Type type = get(i).getType();
            if(closestType.typeContains(type)) {
                closestType = type;
            } else if (!type.typeContains(closestType)) {
                throwError("Types of branches do not match in control group");
            }
        }
        setType(closestType);
    }

    public InterpretValue interpretValue() {
        ControlOperatorNode.InterpretControlResult result = getFirst().interpretControl(false, false, null);
        for (int i = 1; i < size(); i++) {
            result = get(i).interpretControl(result.isSuccess, !result.isSuccess, result.value);
        }
        return result.value != null ? result.value : InterpretTuple.EMPTY;
    }

    public void add(Node node) {
        if(!(node instanceof ControlOperatorNode)) {
            node.throwError("Control group can only contain control operators");
        }
        super.add(node);
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

    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
