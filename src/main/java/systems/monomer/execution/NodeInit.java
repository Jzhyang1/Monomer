package systems.monomer.execution;

import systems.monomer.interpreter.InterpretResult;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.operators.*;
import systems.monomer.types.Type;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings({"ClassWithTooManyMethods", "OverlyCoupledClass"})
public interface NodeInit {
    Node definedValueNode(Supplier<InterpretResult> interpret);

    Node controlGroupNode();
    Node ifNode();          Node allNode();         Node anyNode();         Node elseNode();
    Node repeatNode();      Node whileNode();       Node forNode();
    Node returnNode();

    Node boolNode(boolean value);                               Node charNode(Character c);
    Node floatNode(Double f);                                   Node intNode(Integer i);
    Node stringBuilderNode(Collection<? extends Node> list);    Node stringNode(String s);
    Node listNode();                                            Node structureNode();
    Node tupleNode();
    Node linesNode();
    Node blockNode();
    Node mapNode();         Node setNode();         Node rangeNode(boolean startInclusive, boolean stopInclusive);

    Node assertTypeNode();  Node castNode();        Node convertNode();
    Node assignNode();      Node assignModifyNode();
    Node callNode();        Node castToFunctionNode();
    Node fieldNode();       Node indexNode();
    Node spreadNode();
    Node withNode();
    Node thenNode();

    Node genericOperatorNode(
            String name,
            Function<OperatorNode, Type> type
    );

    Node moduleNode(String name);
    Node variableNode(String name);
}
