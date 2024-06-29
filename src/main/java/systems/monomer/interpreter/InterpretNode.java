package systems.monomer.interpreter;

import systems.monomer.syntaxtree.Node;

import java.util.List;

public interface InterpretNode {
    InterpretVariable interpretVariable();
    InterpretResult interpretValue();

    /**
     * Takes a value and attempts to assign it to the variable
     * that this node represents. If the node is not a variable,
     * an error is thrown. If the variable is locked, an error is thrown.
     * @param value the value to assign
     * @param lock whether to lock the variable after assignment
     * @return whether the assignment was successful
     */
    default boolean interpretAssign(InterpretValue value, boolean lock) {
        InterpretVariable variable = interpretVariable();

        if (variable == null || variable.isLocked()) return false;

        variable.setValue(value);
        if(lock) variable.lock();
        return true;
    }


    Node get(int i);
    List<? extends Node> getChildren();

    default InterpretNode getInterpretNode(int i) {
        return (InterpretNode) get(i);
    }
    default InterpretNode getFirstInterpretNode() {
        return (InterpretNode) get(0);
    }
    default InterpretNode getSecondInterpretNode() {
        return (InterpretNode) get(1);
    }

    default List<InterpretNode> getChildrenInterpretNodes() {
        return (List<InterpretNode>) (List) getChildren();
    }
}
