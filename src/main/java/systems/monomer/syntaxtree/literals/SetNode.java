package systems.monomer.syntaxtree.literals;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretList;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretSet;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SetNode extends LiteralNode {

    public SetNode(){
        super("set");
    }

    public SetNode(Collection<Node> x) {
        super("set");
        getChildren().addAll(x);
    }

    public InterpretResult interpretValue() {
        List<InterpretValue> ret = new ArrayList<>();
        for(Node child : getChildren()) {
            InterpretResult result = child.interpretValue();
            if (!result.isValue()) return result;
            ret.add(result.asValue());
        }
        return new InterpretSet(ret);
    }

    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
