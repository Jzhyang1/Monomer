package systems.monomer.syntaxtree;

import lombok.experimental.UtilityClass;
import systems.monomer.interpreter.InterpretModuleNode;
import systems.monomer.interpreter.InterpretVariableNode;
import systems.monomer.interpreter.controls.*;
import systems.monomer.interpreter.literals.*;
import systems.monomer.interpreter.operators.*;
import systems.monomer.syntaxtree.controls.*;
import systems.monomer.syntaxtree.literals.*;
import systems.monomer.syntaxtree.operators.*;
import systems.monomer.types.Type;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@UtilityClass
public class Configuration {
    public interface Initializer {

        public enum InitializerEnum {
            INTERPRET, COMPILE, DOC;
        }

        InitializerEnum getType();

        Node definedValueNode();

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
                Function<OperatorNode, Type> type
        );

        ModuleNode moduleNode(String name);
        VariableNode variableNode(String name);
    }
    private class Interpret implements Initializer {
        public InitializerEnum getType() {
            return InitializerEnum.INTERPRET;
        }

        public Node definedValueNode() {
            return null;
        }

        public ControlGroupNode controlGroupNode() {
            return new InterpretControlGroupNode();
        }
        public IfNode ifNode() {
            return new InterpretIfNode();
        }
        public AllNode allNode() {
            return new InterpretAllNode();
        }
        public AnyNode anyNode() {
            return new InterpretAnyNode();
        }
        public ElseNode elseNode() {
            return new InterpretElseNode();
        }
        public RepeatNode repeatNode() {
            return new InterpretRepeatNode();
        }
        public WhileNode whileNode() {
            return new InterpretWhileNode();
        }
        public ForNode forNode() {
            return new InterpretForNode();
        }
        public ReturnNode returnNode() {
            return new InterpretReturnNode();
        }
        public BoolNode boolNode(boolean value) {
            return new InterpretBoolNode(value);
        }
        public CharNode charNode(Character c) {
            return new InterpretCharNode(c);
        }
        public FloatNode floatNode(Double f) {
            return new InterpretFloatNode(f);
        }
        public IntNode intNode(Integer i) {
            return new InterpretIntNode(i);
        }
        public StringBuilderNode stringBuilderNode(Collection<? extends Node> list) {
            return new InterpretStringBuilderNode(list);
        }
        public StringNode stringNode(String s) {
            return new InterpretStringNode(s);
        }
        public ListNode listNode() {
            return new InterpretListNode();
        }
        public TupleNode tupleNode() {
            return new InterpretTupleNode();
        }
        public TupleNode blockNode() {
            return new InterpretTupleNode("block");
        }
        public TupleNode linesNode() {
            return new InterpretTupleNode(";");
        }
        public StructureNode structureNode() {
            return new InterpretStructureNode();
        }
        public MapNode mapNode() {
            return new InterpretMapNode();
        }
        public SetNode setNode() {
            throw new RuntimeException("Set has not been implemented");
        }
        public RangeNode rangeNode(boolean startInclusive, boolean stopInclusive) {
            return new InterpretRangeNode(startInclusive, stopInclusive);
        }
        public AssertTypeNode assertTypeNode() {
            return new InterpretAssertTypeNode();
        }
        public CastNode castNode() {
            throw new RuntimeException("Cast has not been implemented");
        }
        public ConvertNode convertNode() {
            throw new RuntimeException("Convert has not been implemented");
        }
        public AssignNode assignNode() {
            return new InterpretAssignNode();
        }
        public AssignModifyNode assignModifyNode() {
            throw new RuntimeException("AssignModify has not been implemented");
        }
        public CallNode callNode() {
            return new InterpretCallNode();
        }
        public CastToFunctionNode castToFunctionNode() {
            return new InterpretCastToFunctionNode();
        }
        public FieldNode fieldNode() {
            return new InterpretFieldNode();
        }
        public IndexNode indexNode() {
            return new InterpretIndexNode();
        }
        public SpreadNode spreadNode() {
            throw new RuntimeException("Spread has not been implemented");
        }
        public WithThenNode withThenNode(String name) {
            return new InterpretWithThenNode(name);
        }
        public GenericOperatorNode genericOperatorNode(
                String name,
                Function<OperatorNode, Type> type
        ) {
            return new InterpretOperatorNode(name, type);
        }

        public ModuleNode moduleNode(String name) {
            return new InterpretModuleNode(name);
        }
        public VariableNode variableNode(String name) {
            return new InterpretVariableNode(name);
        }
    }

    public final Initializer INTERPRET = new Interpret();

    private Initializer action;
    public static Initializer create() {
        return action;
    }
    public void setAction(Initializer interpretCompileEtc) {
        action = interpretCompileEtc;
    }
}
