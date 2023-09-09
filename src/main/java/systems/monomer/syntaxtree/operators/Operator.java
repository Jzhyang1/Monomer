package systems.monomer.syntaxtree.operators;

import lombok.NonNull;
import systems.monomer.Constants;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.*;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.controls.*;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.types.AnyType;
import systems.monomer.types.BoolType;
import systems.monomer.types.Type;
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
    private static final Map<String, Operator> operators = new HashMap<>();
    private static final int NONE = 0;
    private static final int BINARY = 0b1, PREFIX = 0b10, SUFFIX = 0b100, CHAINED = 0b1000, ASSIGN = 0b10000;
    private static final int CONTROL = PREFIX | CHAINED | 0b100000;
    private static final int PRIMARY_CONTROL = CONTROL | 0b10000000, SECONDARY_CONTROL = CONTROL | 0b100000000;
    private static final int WORD = 0b1000000000;


    private static int fillInfo(int info, String symbol) {
        if(Constants.isIdentifierChar(symbol.charAt(0))) {
            info |= WORD;
        }
        return info;
    }
    /**
     * @param leftPrec  a higher number signals a higher precedence
     * @param rightPrec a higher number signals a higher precedence
     */
    private static void putData(String symbol, int leftPrec, int rightPrec, int info, Supplier<Node> constructor) {
        operators.put(symbol, new Operator(fillInfo(info, symbol), leftPrec, rightPrec, constructor));
    }
    private static void putData(String symbol, int prec, int info, Supplier<Node> constructor) {
        operators.put(symbol, new Operator(fillInfo(info, symbol), prec, prec, constructor));
    }
    private static void putData(String symbol, int prec, int info, Function<GenericOperatorNode, CompileValue> compile, Function<GenericOperatorNode, InterpretValue> interpret) {
        operators.put(symbol, new Operator(fillInfo(info, symbol), prec, prec, () -> new GenericOperatorNode(symbol, interpret, compile, (self)-> AnyType.ANY)));
    }
    private static void putData(String symbol, int prec, int info, Function<GenericOperatorNode, CompileValue> compile, Function<GenericOperatorNode, InterpretValue> interpret, Function<GenericOperatorNode, Type> type) {
        operators.put(symbol, new Operator(fillInfo(info, symbol), prec, prec, () -> new GenericOperatorNode(symbol, interpret, compile, type)));
    }
    private static void putData(String symbol, int leftPrec, int rightPrec, int info, Function<GenericOperatorNode, CompileValue> compile, Function<GenericOperatorNode, InterpretValue> interpret) {
        operators.put(symbol, new Operator(fillInfo(info, symbol), leftPrec, rightPrec, () -> new GenericOperatorNode(symbol, interpret, compile, (self)-> AnyType.ANY)));
    }
    private static void putData(String symbol, int prec, int info, Function<GenericOperatorNode, CompileValue> compile, BiFunction<InterpretValue, InterpretValue, InterpretValue> interpret) {
        operators.put(symbol, new Operator(fillInfo(info, symbol), prec, prec, () -> new GenericOperatorNode(symbol,
                (self) -> interpret.apply(self.getFirst().interpretValue(), self.getSecond().interpretValue()),
                compile,
                (self)-> AnyType.ANY
        )));
    }

    /**
     * Arithmetic operators inhabit the precedence range 1000-1200
     */
    private static void initArithmetic() {
        putData("+", 1050, BINARY, (self) -> {
            //TODO
            return null;
        }, numericalChecked(differentiatedIntFloat((a, b)->a+b, (a, b)->a+b)), Arithmetic::typeFor); //TODO positive oper
        putData("-", 1050, BINARY, (self) -> {
            //TODO
            return null;
        }, numericalChecked(differentiatedIntFloat((a, b)->a-b, (a, b)->a-b)), Arithmetic::typeFor); //TODO negative oper
        putData("*", 1055, BINARY, (self) -> {
            //TODO
            return null;
        }, numericalChecked(differentiatedIntFloat((a, b)->a*b, (a, b)->a*b)), Arithmetic::typeFor);
        putData("/", 1055, BINARY, (self) -> {
            //TODO
            return null;
        }, numericalChecked(differentiatedIntFloat((a, b)->a/b, (a, b)->a/b)), Arithmetic::typeFor);
        putData("%", 1055, BINARY, (self) -> {
            //TODO
            return null;
        }, numericalChecked(differentiatedIntFloat((a, b)->b==0?0:a%b, (a, b)->b==0?0:a%b)), Arithmetic::typeFor);
        putData("||", 1065, BINARY, (self) -> {
            //TODO
            return null;
        }, numericalChecked(alwaysFloat((a, b)->a*b/(a+b))), Arithmetic::typeFor);
        putData("**", 1075, BINARY, (self) -> {
            //TODO
            return null;
        }, numericalChecked(differentiatedIntFloat((a, b)->(int)StrictMath.pow(a,b), StrictMath::pow)), Arithmetic::typeFor);
        putData("*/", 1075, BINARY, (self) -> {
            //TODO
            return null;
        }, numericalChecked(differentiatedIntFloat((a, b)->(int)StrictMath.pow(a,1.0/b), (a, b)->StrictMath.pow(a,1.0/b))), Arithmetic::typeFor);
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
        //TODO replace (self) -> BoolType.BOOL with a named function that also handles non-bool
        putData("!", 860, PREFIX, (self)->null, oneBool((a)->!a), (self) -> BoolType.BOOL);
        putData("?", 860, PREFIX, (self)->null, isTruthy(), (self) -> BoolType.BOOL);
        putData("&", 850, BINARY, (self)->null, differentiatedIntBool((a,b)->a&b, (a, b)->a&&b), (self) -> BoolType.BOOL);
        putData("|", 820, BINARY, (self)->null, differentiatedIntBool((a,b)->a|b, (a, b)->a||b), (self) -> BoolType.BOOL);
        putData("^", 820, BINARY, (self)->null, differentiatedIntBool((a,b)->a^b, (a, b)->a^b), (self) -> BoolType.BOOL);
        putData("~&", 850, BINARY, (self)->null, differentiatedIntBool((a,b)->~(a&b), (a, b)->!(a&&b)), (self) -> BoolType.BOOL);
        putData("~|", 820, BINARY, (self)->null, differentiatedIntBool((a,b)->~(a|b), (a, b)->!(a||b)), (self) -> BoolType.BOOL);
        putData("~^", 820, BINARY, (self)->null, differentiatedIntBool((a,b)->~(a^b), (a, b)->a==b), (self) -> BoolType.BOOL);
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
        putData(".", 430, BINARY | CHAINED, (self)->null, listStringChecked(
                (lists)->
                        //TODO not just lists
                        new InterpretList(lists.stream().flatMap((list)->list.getValues().stream()).collect(Collectors.toList())),
                (strs)-> new InterpretString(strs.stream().map((str)->str.getValue()).collect(Collectors.joining()))),
                (self)->self.getFirst().getType()); //TODO fix
        putData("...", 440, BINARY, (self)->null, isTruthy());
        putData("in", 820, BINARY, (self)->null, (self)->new InterpretBool(((InterpretCollection)self.getSecond().interpretValue()).getValues().contains(self.getFirst().interpretValue()))); //TODO fix and clean
        putData("#", 1800, PREFIX, (self)->null, listStringChecked(
                (list)->new InterpretNumber<>(list.get(0).getValues().size()),
                (strs)->new InterpretNumber<>(strs.get(0).getValue().length())),
                (self)->self.getFirst().getType() //TODO fix
        );
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
        putData("=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("+=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("-=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("*=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("/=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("%=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("&=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("|=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("^=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
        putData(",", 100, BINARY | CHAINED | SUFFIX, ()->new TupleNode(","));
        putData(";", -1000, BINARY | CHAINED | SUFFIX, ()->new TupleNode(";"));
        putData(":", 1500, 150, BINARY, AssertTypeNode::new);
        putData("as", 5, 5, BINARY, ConvertNode::new);
        putData("@", 5000, PREFIX, (self) -> {
            //TODO
            return null;
        }, (self) -> {
            InterpretValue first = self.getFirst().interpretValue();
            System.out.println(first.valueString());
            return first;
        }, (self) -> self.getFirst().getType());
        putData("with", -5, PREFIX, (self) -> {
            //TODO
            return null;
        }, (self) -> {
            InterpretValue first = self.getFirst().interpretValue();
            self.getSecond().interpretValue();
            return first;
        }, (self) -> self.getFirst().getType());
        putData("then", -5, PREFIX, (self) -> {
            //TODO
            return null;
        }, (self) -> {
            self.getFirst().interpretValue();
            return self.getSecond().interpretValue();
        }, (self) -> self.getSecond().getType());

        initComparison();
        initBitwise();
        initArithmetic();
        initList();
        initControl();

        Set<String> tempSymbols = operators.entrySet().stream().filter((entry)->(entry.getValue().info & WORD) != WORD).map(Map.Entry::getKey).collect(Collectors.toSet());

        symbolOperatorSet = tempSymbols;
        wordOperatorSet = operators.entrySet().stream().filter((entry)->(entry.getValue().info & WORD) == WORD).map(Map.Entry::getKey).collect(Collectors.toSet());
        startingSymobolOperatorCharacterSet = tempSymbols.stream().map((s)->s.charAt(0)).collect(Collectors.toSet());
    }
    public static Node getOperator(String name) {
        return operators.get(name).getOperator();
    }

    private static final Set<String> symbolOperatorSet;
    public static Set<String> symbolOperators() {
        return symbolOperatorSet;
    }
    private static final Set<String> wordOperatorSet;
    public static Set<String> wordOperators() {
        return wordOperatorSet;
    }

    /**
     * optimization for the lexer
     */
    private static final Set<Character> startingSymobolOperatorCharacterSet;
    public static Set<Character> startingSymbolOperatorCharacters() {
        return startingSymobolOperatorCharacterSet;
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
    public static boolean isAssign(String symbol) {
        return (operators.get(symbol).info & ASSIGN) == ASSIGN;
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
