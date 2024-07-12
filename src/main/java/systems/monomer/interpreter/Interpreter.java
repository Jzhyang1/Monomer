package systems.monomer.interpreter;

import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.assembly.Operand;
import systems.monomer.compiler.operators.CompileOperatorNode;
import systems.monomer.execution.Initializer;
import systems.monomer.execution.environmentDefaults.ConvertDefaults;
import systems.monomer.execution.environmentDefaults.FileDefaults;
import systems.monomer.execution.environmentDefaults.TypeDefaults;
import systems.monomer.execution.environmentDefaults.ValueDefaults;
import systems.monomer.interpreter.controls.*;
import systems.monomer.interpreter.literals.*;
import systems.monomer.interpreter.operators.*;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.VariableNode;
import systems.monomer.syntaxtree.controls.*;
import systems.monomer.syntaxtree.literals.*;
import systems.monomer.syntaxtree.operators.*;
import systems.monomer.tokenizer.Source;
import systems.monomer.tokenizer.Token;
import systems.monomer.types.Type;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class Interpreter implements Initializer {
    public static void interpret(Source source, boolean defaults, InputStream input, OutputStream output) {
        Node.init = new Interpreter();

        Token body = source.parse();
        Node node = body.toNode();
        InterpretModuleNode global = (InterpretModuleNode) Node.init.moduleNode(source.getTitle());

        //global constants here
        if(defaults) {
            TypeDefaults.initGlobal(global);
            ValueDefaults.initGlobal(global);
            FileDefaults.initGlobal(global, input, output);
            ConvertDefaults.initGlobal(global);
        }

        global.add(node);

        global.matchVariables();
        global.matchTypes();
        global.setIsExpression(false);
        global.interpretValue();
    }


    public Node definedValueNode(Supplier<InterpretResult> interpret) {
        return new InterpretDefinedValueNode(interpret);
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
            Function<OperatorNode, Type> type,
            BiFunction<CompileOperatorNode, AssemblyFile, Operand> compile,
            Function<InterpretOperatorNode, ? extends InterpretResult> interpret
    ) {
        InterpretOperatorNode ret = new InterpretOperatorNode(name, type);
        ret.setInterpretGenerator(interpret);
        return ret;
    }

    public ModuleNode moduleNode(String name) {
        return new InterpretModuleNode(name);
    }
    public VariableNode variableNode(String name) {
        return new InterpretVariableNode(name);
    }
}
