package systems.monomer.syntaxtree.literals;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretList;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.ListType;
import systems.monomer.types.Type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListNode extends LiteralNode {

    public ListNode() {
        super("list");
    }

    public ListNode(Collection<? extends Node> list) {
        super("list");
        addAll(list);
    }

    public void matchTypes() {
        super.matchTypes();
        if(getChildren().isEmpty()) {
            setType(ListType.LIST);
        }
        else {
            Type t = get(0).getType();
            for(int i = size() - 1; i >= 0; --i) {
                if(!t.typeContains(get(i).getType())) {
                    throwError("Types of elements in list do not match");
                }
            }
            setType(new ListType(t));
        }
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

    public Operand compileValue(AssemblyFile file) {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
