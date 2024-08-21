package systems.monomer.interpreter;

import systems.monomer.compiler.operators.CompileOperatorNode;
import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.output.CompileValue;
import systems.monomer.execution.Initializer;
import systems.monomer.execution.environmentDefaults.ConvertDefaults;
import systems.monomer.execution.environmentDefaults.FileDefaults;
import systems.monomer.execution.environmentDefaults.TypeDefaults;
import systems.monomer.execution.environmentDefaults.ValueDefaults;
import systems.monomer.interpreter.controls.*;
import systems.monomer.interpreter.execution.InterpretUtil;
import systems.monomer.interpreter.literals.*;
import systems.monomer.interpreter.operators.*;
import systems.monomer.interpreter.variables.InterpretFieldKey;
import systems.monomer.interpreter.variables.InterpretIndexKey;
import systems.monomer.interpreter.variables.InterpretKey;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.operators.GenericOperatorNode;
import systems.monomer.syntaxtree.operators.IndexNode;
import systems.monomer.syntaxtree.operators.OperatorNode;
import systems.monomer.tokenizer.Source;
import systems.monomer.tokenizer.Token;
import systems.monomer.types.Type;
import systems.monomer.variables.Key;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class Interpreter extends Initializer<Function<GenericOperatorNode, Function<Iterator<InterpretValue>, ? extends InterpretResult>>> {
    public static void interpret(Source source, boolean defaults, InputStream input, OutputStream output) {
        Initializer init = new Interpreter();
        source.with(init);

        Token body = source.parse();
        Node node = body.toNode();
        InterpretModuleNode global = (InterpretModuleNode) init.moduleNode(source.getTitle());

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

    private Map<String, Function<GenericOperatorNode, Function<Iterator<InterpretValue>, ? extends InterpretResult>>> operatorBodies = new HashMap<>();
    public Interpreter() {
        //purposefully omitted are the control operators, assignment, cast, convert, and with/then
        operatorBodies.put("+", InterpretUtil.unBi(InterpretUtil.intFloatUniOp((a)->+a, (b)->+b), InterpretUtil.intFloatOp((a, b) -> a + b, (a, b) -> a + b)));
        operatorBodies.put("-", InterpretUtil.unBi(InterpretUtil.intFloatUniOp((a)->-a, (b)->-b), InterpretUtil.intFloatOp((a, b) -> a - b, (a, b) -> a - b)));
        operatorBodies.put("*", InterpretUtil.intFloatOp((a, b) -> a * b, (a, b) -> a * b));
        operatorBodies.put("/", InterpretUtil.intFloatOp((a, b) -> a / b, (a, b) -> a / b));
        operatorBodies.put("%", InterpretUtil.intFloatOp((a, b) -> b == 0 ? 0 : a % b, (a, b) -> b == 0 ? 0 : a % b));
        operatorBodies.put("||", InterpretUtil.intFloatOp((a, b) -> a * b / (a + b), (a, b) -> a * b / (a + b)));
        operatorBodies.put("**", InterpretUtil.intFloatOp((a, b) -> (int) StrictMath.pow(a, b), StrictMath::pow));
        operatorBodies.put("*/", InterpretUtil.intFloatOp((a, b) -> (int) StrictMath.pow(a, 1.0 / b), (a, b) -> StrictMath.pow(a, 1.0 / b)));

        operatorBodies.put("!", InterpretUtil.boolOp((a) -> !a));
        operatorBodies.put("?", InterpretUtil.truthyOp());
        operatorBodies.put("&", InterpretUtil.intBoolOp((a, b) -> a & b, (a, b) -> a && b));
        operatorBodies.put("|", InterpretUtil.intBoolOp((a, b) -> a | b, (a, b) -> a || b));
        operatorBodies.put("^", InterpretUtil.intBoolOp((a, b) -> a ^ b, (a, b) -> a ^ b));
        operatorBodies.put("~&", InterpretUtil.intBoolOp((a, b) -> ~(a & b), (a, b) -> !(a && b)));
        operatorBodies.put("~|", InterpretUtil.intBoolOp((a, b) -> ~(a | b), (a, b) -> !(a || b)));
        operatorBodies.put("~^", InterpretUtil.intBoolOp((a, b) -> ~(a ^ b), (a, b) -> a == b));

        operatorBodies.put("==", InterpretUtil.cmpOp((a, b) -> a == b, (a, b) -> a == b, (a, b) -> a == b, (a, b) -> a.equals(b)));
        operatorBodies.put("!=", InterpretUtil.cmpOp((a, b) -> a != b, (a, b) -> a != b, (a, b) -> a != b, (a, b) -> !a.equals(b)));
        operatorBodies.put(">", InterpretUtil.cmpOp((a, b) -> a < b, (a, b) -> a < b, (a, b) -> a && !b, (a, b) -> false /*TODO*/));
        operatorBodies.put("<", InterpretUtil.cmpOp((a, b) -> a > b, (a, b) -> a > b, (a, b) -> !a && b, (a, b) -> false /*TODO*/));
        operatorBodies.put(">=", InterpretUtil.cmpOp((a, b) -> a <= b, (a, b) -> a <= b, (a, b) -> a || !b, (a, b) -> false /*TODO*/));
        operatorBodies.put("<=", InterpretUtil.cmpOp((a, b) -> a >= b, (a, b) -> a >= b, (a, b) -> !a || b, (a, b) -> false /*TODO*/));
        //operatorBodies.put("?=", null); //TODO

        operatorBodies.put(".", InterpretUtil.colOp((col1, col2)->{
                col1.addAll(col2);
                return col1;
            }));
        operatorBodies.put("...", InterpretUtil.unBi(InterpretUtil.spreadOp(), InterpretUtil.rangeOp()));
        operatorBodies.put("in", InterpretUtil.inOp());
        operatorBodies.put("#", InterpretUtil.sizeOp());

        operatorBodies.put("break", InterpretUtil.breakingOp());
        operatorBodies.put("continue", InterpretUtil.breakingOp());
        operatorBodies.put("return", InterpretUtil.breakingOp());
    }

    public Node definedValueNode(Supplier<InterpretResult> interpret) {
        return new InterpretDefinedValueNode(interpret).with(this);
    }

    public Node controlGroupNode() {
        return new InterpretControlGroupNode().with(this);
    }
    public Node ifNode() {
        return new InterpretIfNode().with(this);
    }
    public Node allNode() {
        return new InterpretAllNode().with(this);
    }
    public Node anyNode() {
        return new InterpretAnyNode().with(this);
    }
    public Node elseNode() {
        return new InterpretElseNode().with(this);
    }
    public Node repeatNode() {
        return new InterpretRepeatNode().with(this);
    }
    public Node whileNode() {
        return new InterpretWhileNode().with(this);
    }
    public Node forNode() {
        return new InterpretForNode().with(this);
    }
    public Node returnNode() {
        return new InterpretReturnNode().with(this);
    }
    public Node boolNode(boolean value) {
        return new InterpretBoolNode(value).with(this);
    }
    public Node charNode(Character c) {
        return new InterpretCharNode(c).with(this);
    }
    public Node floatNode(Double f) {
        return new InterpretFloatNode(f).with(this);
    }
    public Node intNode(Integer i) {
        return new InterpretIntNode(i).with(this);
    }
    public Node stringBuilderNode(Collection<? extends Node> list) {
        return new InterpretStringBuilderNode(list).with(this);
    }
    public Node stringNode(String s) {
        return new InterpretStringNode(s).with(this);
    }
    public Node listNode() {
        return new InterpretListNode().with(this);
    }
    public Node tupleNode() {
        return new InterpretTupleNode().with(this);
    }
    public Node blockNode() {
        return new InterpretTupleNode("block").with(this);
    }
    public Node linesNode() {
        return new InterpretTupleNode(";").with(this);
    }
    public Node structureNode() {
        return new InterpretStructureNode().with(this);
    }
    public Node mapNode() {
        return new InterpretMapNode().with(this);
    }
    public Node setNode() {
        throw new RuntimeException("Set has not been implemented");
    }
    public Node rangeNode(boolean startInclusive, boolean stopInclusive) {
        return new InterpretRangeNode(startInclusive, stopInclusive).with(this);
    }
    public Node assertTypeNode() {
        return new InterpretAssertTypeNode().with(this);
    }
    public Node castNode() {
        throw new RuntimeException("Cast has not been implemented");
    }
    public Node convertNode() {
        throw new RuntimeException("Convert has not been implemented");
    }
    public Node assignNode() {
        return new InterpretAssignNode().with(this);
    }
    public Node assignModifyNode() {
        throw new RuntimeException("AssignModify has not been implemented");
    }
    public Node callNode() {
        return new InterpretCallNode().with(this);
    }
    public Node castToFunctionNode() {
        return new InterpretCastToFunctionNode().with(this);
    }
    public Node fieldNode() {
        return new InterpretFieldNode().with(this);
    }
    public Node indexNode() {
        return new InterpretIndexNode().with(this);
    }
    public Node spreadNode() {
        throw new RuntimeException("Spread has not been implemented");
    }
    public Node withNode() {
        return new InterpretWithNode().with(this);
    }
    public Node thenNode() {
        return new InterpretThenNode().with(this);
    }
    public Node genericOperatorNode(
            String name,
            Function<OperatorNode, Type> type
    ) {
        return new InterpretOperatorNode(name, type).with(this);
    }

    public Node moduleNode(String name) {
        return new InterpretModuleNode(name).with(this);
    }
    public Node variableNode(String name) {
        return new InterpretVariableNode(name).with(this);
    }

    public Key variableKey() {
        return new InterpretKey().with(this);
    }
    public Key fieldKey(String name, Key parent) {
        return new InterpretFieldKey(name, parent).with(this);
    }
    public Key indexKey(IndexNode owner) {
        return new InterpretIndexKey(owner).with(this);
    }

    @Override
    public Function<GenericOperatorNode, Function<Iterator<InterpretValue>, ? extends InterpretResult>> getOperatorBody(String symbol) {
        return operatorBodies.get(symbol);
    }
}
