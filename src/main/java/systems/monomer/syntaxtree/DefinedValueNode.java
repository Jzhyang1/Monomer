package systems.monomer.syntaxtree;

public class DefinedValueNode extends Node {
    public DefinedValueNode() {
        super(DefinedValueNode.class.getName());
    }

    @Override
    public Usage getUsage() {
        return Usage.LITERAL;
    }
}
