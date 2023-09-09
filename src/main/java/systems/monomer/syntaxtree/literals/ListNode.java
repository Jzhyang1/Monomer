package systems.monomer.syntaxtree.literals;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretList;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.ListType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListNode extends LiteralNode {

    public ListNode() {
        super("list");
    }

    public ListNode(Collection<? extends Node> list) {
        super("list");
        getChildren().addAll(list);
    }

    public void matchTypes() {
        super.matchTypes();
        setType(ListType.LIST);
    }

    public InterpretResult interpretValue() {
        List<InterpretValue> ret = new ArrayList<>();
        for(Node child : getChildren()) {
            InterpretResult result = child.interpretValue();
            if (!result.isValue()) return result;
            ret.add(result.asValue());
        }
        return new InterpretList(ret);
    }

    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
