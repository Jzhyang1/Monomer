package systems.monomer.compiler;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.syntaxtree.Node;

import java.util.List;

public interface CompileNode {
    Operand compileValue(AssemblyFile file);
    CompileSize compileSize();
    void compileVariables(AssemblyFile file);

    List<? extends Node> getChildren();
    default List<CompileNode> getChildrenCompileNodes() {
        return (List<CompileNode>) getChildren();
    }

    default CompileNode getCompileNode(int i) {
        return getChildrenCompileNodes().get(i);
    }
    default CompileNode getFirstCompileNode(){
        return getCompileNode(0);
    }
    default CompileNode getSecondCompileNode(){
        return getCompileNode(1);
    }
}
