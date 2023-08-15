package systems.monomer.syntaxtree.operators;

import lombok.NonNull;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.*;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.controls.*;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.util.Pair;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static systems.monomer.syntaxtree.operators.Arithmetic.*;
import static systems.monomer.syntaxtree.operators.Bitwise.*;
import static systems.monomer.syntaxtree.operators.Lists.*;

public final class Operator {
    private static Map<String, Operator> operators = new HashMap<>();
    private static final int NONE = 0;
    private static final int BINARY = 1, PREFIX = 2, SUFFIX = 4, CHAINED = 8;
    private static final int CONTROL = PREFIX | CHAINED | 32;
    private static final int PRIMARY_CONTROL = CONTROL | 64, SECONDARY_CONTROL = CONTROL | 128;

    /**
     * @param leftPrec  a higher number signals a higher precedence
     * @param rightPrec a higher number signals a higher precedence
     */
    private static void putData(String symbol, int leftPrec, int rightPrec, int info, Supplier<Node> constructor) {
        operators.put(symbol, new Operator(info, leftPrec, rightPrec, constructor));
    }
    private static void putData(String symbol, int prec, int info, Supplier<Node> constructor) {
        operators.put(symbol, new Operator(info, prec, prec, constructor));
    }
    private static void putData(String symbol, int prec, int info, Function<GenericOperatorNode, CompileValue> compile, Function<GenericOperatorNode, InterpretValue> interpret) {
        operators.put(symbol, new Operator(info, prec, prec, () -> new GenericOperatorNode(symbol) {{
            setCompile(compile);
            setInterpret(interpret);
        }}));
    }
    private static void putData(String symbol, int leftPrec, int rightPrec, int info, Function<GenericOperatorNode, CompileValue> compile, Function<GenericOperatorNode, InterpretValue> interpret) {
        operators.put(symbol, new Operator(info, leftPrec, rightPrec, () -> new GenericOperatorNode(symbol) {{
            setCompile(compile);
            setInterpret(interpret);
        }}));
    }
    private static void putData(String symbol, int prec, int info, Function<GenericOperatorNode, CompileValue> compile, BiFunction<InterpretValue, InterpretValue, InterpretValue> interpret) {
        operators.put(symbol, new Operator(info, prec, prec, () -> new GenericOperatorNode(symbol) {{
            setCompile(compile);
            setInterpret((self) -> interpret.apply(self.getFirst().interpretValue(), self.getSecond().interpretValue()));
        }}));
    }

    /**
     * Arithmetic operators inhabit the precedence range 1000-1200
     */
    private static void initArithmetic() {
        putData("+", 1050, BINARY, (self) -> {
            //TODO
            return null;
        }, numericalChecked(differentiatedIntFloat((a, b)->a+b, (a, b)->a+b))); //TODO positive oper
        putData("-", 1050, BINARY, (self) -> {
            //TODO
            return null;
        }, numericalChecked(differentiatedIntFloat((a, b)->a-b, (a, b)->a-b))); //TODO negative oper
        putData("*", 1055, BINARY, (self) -> {
            //TODO
            return null;
        }, numericalChecked(differentiatedIntFloat((a, b)->a*b, (a, b)->a*b)));
        putData("/", 1055, BINARY, (self) -> {
            //TODO
            return null;
        }, numericalChecked(differentiatedIntFloat((a, b)->a/b, (a, b)->a/b)));
        putData("%", 1055, BINARY, (self) -> {
            //TODO
            return null;
        }, numericalChecked(differentiatedIntFloat((a, b)->a%b, (a, b)->a%b)));
        putData("||", 1065, BINARY, (self) -> {
            //TODO
            return null;
        }, numericalChecked(alwaysFloat((a, b)->a*b/(a+b))));
        putData("**", 1075, BINARY, (self) -> {
            //TODO
            return null;
        }, numericalChecked(differentiatedIntFloat((a, b)->(int)StrictMath.pow(a,b), StrictMath::pow)));
        putData("*/", 1075, BINARY, (self) -> {
            //TODO
            return null;
        }, numericalChecked(differentiatedIntFloat((a, b)->(int)StrictMath.pow(a,1.0/b), (a, b)->StrictMath.pow(a,1.0/b))));
        putData("><", 1060, BINARY, (self) -> {
            //TODO
            return null;
        }, (self) -> {
            //TODO
            return null;
        });
    }

    /**
     * Bitwise operators inhabit the precedence range 700-900
     */
    private static void initBitwise() {
        putData("!", 860, PREFIX, (self)->null, oneBool((a)->!a));
        putData("?", 860, PREFIX, (self)->null, isTruthy());
        putData("&", 850, BINARY, (self)->null, differentiatedIntBool((a,b)->a&b, (a, b)->a&&b));
        putData("|", 820, BINARY, (self)->null, differentiatedIntBool((a,b)->a|b, (a, b)->a||b));
        putData("^", 820, BINARY, (self)->null, differentiatedIntBool((a,b)->a^b, (a, b)->a^b));
        putData("~&", 850, BINARY, (self)->null, differentiatedIntBool((a,b)->~(a&b), (a, b)->!(a&&b)));
        putData("~|", 820, BINARY, (self)->null, differentiatedIntBool((a,b)->~(a|b), (a, b)->!(a||b)));
        putData("~^", 820, BINARY, (self)->null, differentiatedIntBool((a,b)->~(a^b), (a, b)->a==b));
    }

    /**
     * Comparison operators inhabit the precedence range 500-600
     */
    private static void initComparison() {
        putData("==", 550, BINARY | CHAINED, (self) -> {
            //TODO
            return null;
        }, Comparison.generalIntFloatCharString(Objects::equals));
        putData("!=", 550, BINARY | CHAINED, (self) -> {
            //TODO
            return null;
        }, Comparison.generalIntFloatCharString((a,b)-> !Objects.equals(a, b)));
        putData(">", 550, BINARY | CHAINED, (self) -> {
            //TODO
            return null;
        }, Comparison.generalIntFloatCharString((a,b)-> a.compareTo(b)>0));
        putData("<", 550, BINARY | CHAINED, (self) -> {
            //TODO
            return null;
        }, Comparison.generalIntFloatCharString((a,b)-> a.compareTo(b)<0));
        putData(">=", 550, BINARY | CHAINED, (self) -> {
            //TODO
            return null;
        }, Comparison.generalIntFloatCharString((a,b)-> a.compareTo(b)>=0));
        putData("<=", 550, BINARY | CHAINED, (self) -> {
            //TODO
            return null;
        }, Comparison.generalIntFloatCharString((a,b)-> a.compareTo(b)<=0));
        putData("?=", 555, BINARY, (self) -> {
            //TODO
            return null;
        }, (first, second) -> {
            return null;
        });
    }

    /**
     * List operators inhabit the precedence range 400-500 with 1 exception
     */
    private static void initList() {
        putData(".", 430, BINARY | CHAINED, (self)->null, listChecked(
                (lists)->
                        //TODO not just lists
                        new InterpretList(lists.stream().flatMap((list)->list.getValues().stream()).collect(Collectors.toList()))
                ));
        putData("...", 440, BINARY, (self)->null, isTruthy());
        putData("in", 820, BINARY, (self)->null, (self)->new InterpretBool(((InterpretCollectionValue)self.getSecond().interpretValue()).getValues().contains(self.getFirst().interpretValue()))); //TODO fix and clean
        putData("#", 1800, PREFIX, (self)->null, listChecked((list)->new InterpretNumberValue<>(list.get(0).getValues().size())));
    }

    /**
     * Control operators inhabit the precedence range -100-50
     * This range overlaps with some other operators
     */
    private static void initControl() {
        putData("if", -20, PRIMARY_CONTROL, IfNode::new);
        putData("repeat", -20, PRIMARY_CONTROL, RepeatNode::new);
        putData("while", -20, PRIMARY_CONTROL, WhileNode::new);
        putData("for", -20, PRIMARY_CONTROL, ForNode::new);
        putData("else", -20, SECONDARY_CONTROL, ElseNode::new);
        putData("any", -20, SECONDARY_CONTROL, AnyNode::new);
        putData("all", -20, SECONDARY_CONTROL, AllNode::new);
//        putData("break", -20, (self)->null, (self)->null);
//        putData("continue", -20, (self)->null, (self)->null);
    }

    static {
        putData("=", 0,  BINARY | CHAINED,AssignNode::new);
        putData(",", 100, BINARY | CHAINED | SUFFIX, ()->new TupleNode(","));
        putData(";", -1000, BINARY | CHAINED | SUFFIX, ()->new TupleNode(";"));
        putData(":", 1500, 150, BINARY, ConvertNode::new);
        putData("as", 5, 5, BINARY, ConvertNode::new);
        putData("@", 5000, PREFIX, (self) -> {
            //TODO
            return null;
        }, (self) -> {
            InterpretValue first = self.getFirst().interpretValue();
            System.out.println(first.valueString());
            return first;
        });

        initComparison();
        initBitwise();
        initArithmetic();
        initList();
        initControl();
    }
    public static Node getOperator(String name) {
        return operators.get(name).getOperator();
    }

    public static Set<String> symbolOperators() {  //TODO
        return operators.keySet();
    }

    public static boolean isSuffix(String symbol) {
        return (operators.get(symbol).info & SUFFIX) == SUFFIX;
    }
    public static boolean isPrefix(String symbol) {
        return (operators.get(symbol).info & PREFIX) == PREFIX;
    }
    public static boolean isBinary(String symbol) {
        return (operators.get(symbol).info & BINARY) == BINARY;
    }
    public static boolean isControl(String symbol) {
        return (operators.get(symbol).info & CONTROL) == CONTROL;
    }
    public static boolean isPrimaryControl(String symbol) {
        return (operators.get(symbol).info & PRIMARY_CONTROL) == PRIMARY_CONTROL;
    }
    public static boolean isSecondaryControl(String symbol) {
        return (operators.get(symbol).info & SECONDARY_CONTROL) == SECONDARY_CONTROL;
    }
    public static boolean isOperator(String symbol) {
        return operators.containsKey(symbol);
    }
    public static boolean isBreaking(String nextToken) {
        return isBinary(nextToken) || isPrefix(nextToken);
    }

    public static Set<Character> signStartDelimiters() {
        HashSet<Character> delimiters = new HashSet<>(List.of('(', '[', '{'));   //TODO
        return delimiters;
    }
    public static Set<Character> signEndDelimiters() {
        HashSet<Character> delimiters = new HashSet<>(List.of(')', ']', '}'));   //TODO
        return delimiters;
    }

    public static Pair<Integer, Integer> precedence(String op) {
        Operator dat = operators.get(op);
        return new Pair<>(dat.leftPrec, dat.rightPrec);
    }

    public static boolean isChained(String a, String b) {
        List<Set<String>> chains = List.of(
                new TreeSet<>(List.of(",")),
                new TreeSet<>(List.of(";")),
                new TreeSet<>(List.of(".")),
                new HashSet<>(List.of("<", "<=", "==")),
                new HashSet<>(List.of(">", ">=", "==")),
                new TreeSet<>(List.of("==", "!=")),
                new HashSet<>(List.of("if", "else", "any", "all")),
                new HashSet<>(List.of("repeat", "else", "any", "all")),
                new HashSet<>(List.of("while", "else", "any", "all")),
                new HashSet<>(List.of("for", "else", "any", "all"))
        );
        return chains.stream().anyMatch((g) -> g.contains(a) && g.contains(b));
    }



    final int leftPrec, rightPrec;
    final int info;
    private final Supplier<Node> constructor;

    private Operator(int info, int leftPrec, int rightPrec, @NonNull Supplier<Node> constructor) {
        this.info = info;
        this.leftPrec = leftPrec;
        this.rightPrec = rightPrec;
        this.constructor = constructor;
    }

    public Node getOperator() {
        return constructor.get();
    }
}
