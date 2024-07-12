package systems.monomer.execution;

import org.jetbrains.annotations.Nullable;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.assembly.Operand;
import systems.monomer.compiler.operators.CompileOperatorNode;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.operators.InterpretOperatorNode;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.VariableNode;
import systems.monomer.syntaxtree.controls.*;
import systems.monomer.syntaxtree.literals.*;
import systems.monomer.syntaxtree.operators.*;
import systems.monomer.types.Type;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings({"ClassWithTooManyMethods", "OverlyCoupledClass"})
public interface Initializer {
    Node definedValueNode(Supplier<InterpretResult> interpret);

    ControlGroupNode controlGroupNode();
    IfNode ifNode(); AllNode allNode(); AnyNode anyNode(); ElseNode elseNode();
    RepeatNode repeatNode(); WhileNode whileNode(); ForNode forNode();
    ReturnNode returnNode();

    BoolNode boolNode(boolean value); CharNode charNode(Character c);
    FloatNode floatNode(Double f); IntNode intNode(Integer i);
    StringBuilderNode stringBuilderNode(Collection<? extends Node> list); StringNode stringNode(String s);
    ListNode listNode(); StructureNode structureNode();
    TupleNode tupleNode(); default TupleNode tupleNode(List<? extends Node> nodes){return (TupleNode) tupleNode().with(nodes);}
    TupleNode linesNode(); default TupleNode linesNode(List<? extends Node> nodes){return (TupleNode) linesNode().with(nodes);}
    TupleNode blockNode(); default TupleNode blockNode(List<? extends Node> nodes){return (TupleNode) blockNode().with(nodes);}
    MapNode mapNode(); SetNode setNode(); RangeNode rangeNode(boolean startInclusive, boolean stopInclusive);

    AssertTypeNode assertTypeNode(); CastNode castNode(); ConvertNode convertNode();
    AssignNode assignNode(); AssignModifyNode assignModifyNode();
    CallNode callNode(); CastToFunctionNode castToFunctionNode();
    FieldNode fieldNode(); IndexNode indexNode();
    SpreadNode spreadNode();
    WithThenNode withThenNode(String name);
    default WithThenNode withNode(){
        return withThenNode("with");
    }
    default WithThenNode thenNode(){
        return withThenNode("then");
    }

    GenericOperatorNode genericOperatorNode(
            String name,
            Function<OperatorNode, Type> type,
            BiFunction<CompileOperatorNode, AssemblyFile, Operand>compile,
            Function<InterpretOperatorNode, ? extends InterpretResult> interpret
    );

    ModuleNode moduleNode(String name);
    VariableNode variableNode(String name);
}
