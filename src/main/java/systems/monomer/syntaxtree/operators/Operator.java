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

public class Operator {
    private static Map<String, Operator> operators = new HashMap<>();

    /**
     * @param leftPrec  a higher number signals a higher precedence
     * @param rightPrec a higher number signals a higher precedence
     */
    private static void putData(String symbol, int leftPrec, int rightPrec, Supplier<Node> constructor) {
        operators.put(symbol, new Operator(leftPrec, rightPrec, constructor));
    }
    private static void putData(String symbol, int prec, Supplier<Node> constructor) {
        operators.put(symbol, new Operator(prec, prec, constructor));
    }
    private static void putData(String symbol, int prec, Function<GenericOperatorNode, CompileValue> compile, Function<GenericOperatorNode, InterpretValue> interpret) {
        operators.put(symbol, new Operator(prec, prec, () -> new GenericOperatorNode(symbol) {{
            setCompile(compile);
            setInterpret(interpret);
        }}));
    }
    private static void putData(String symbol, int leftPrec, int rightPrec, Function<GenericOperatorNode, CompileValue> compile, Function<GenericOperatorNode, InterpretValue> interpret) {
        operators.put(symbol, new Operator(leftPrec, rightPrec, () -> new GenericOperatorNode(symbol) {{
            setCompile(compile);
            setInterpret(interpret);
        }}));
    }
    private static void putData(String symbol, int prec, Function<GenericOperatorNode, CompileValue> compile, BiFunction<InterpretValue, InterpretValue, InterpretValue> interpret) {
        operators.put(symbol, new Operator(prec, prec, () -> new GenericOperatorNode(symbol) {{
            setCompile(compile);
            setInterpret((self) -> interpret.apply(self.getFirst().interpretValue(), self.getSecond().interpretValue()));
        }}));
    }

    /**
     * Arithmetic operators inhabit the precedence range 1000-1200
     */
    private static void initArithmetic() {
        putData("+", 1050, (self) -> {
            //TODO
            return null;
        }, numericalChecked(differentiatedIntFloat((a, b)->a+b, (a, b)->a+b))); //TODO positive oper
        putData("-", 1050, (self) -> {
            //TODO
            return null;
        }, numericalChecked(differentiatedIntFloat((a, b)->a-b, (a, b)->a-b))); //TODO negative oper
        putData("*", 1055, (self) -> {
            //TODO
            return null;
        }, numericalChecked(differentiatedIntFloat((a, b)->a*b, (a, b)->a*b)));
        putData("/", 1055, (self) -> {
            //TODO
            return null;
        }, numericalChecked(differentiatedIntFloat((a, b)->a/b, (a, b)->a/b)));
        putData("%", 1055, (self) -> {
            //TODO
            return null;
        }, numericalChecked(differentiatedIntFloat((a, b)->a%b, (a, b)->a%b)));
        putData("||", 1065, (self) -> {
            //TODO
            return null;
        }, numericalChecked(alwaysFloat((a, b)->a*b/(a+b))));
        putData("**", 1075, (self) -> {
            //TODO
            return null;
        }, numericalChecked(differentiatedIntFloat((a, b)->(int)StrictMath.pow(a,b), StrictMath::pow)));
        putData("*/", 1075, (self) -> {
            //TODO
            return null;
        }, numericalChecked(differentiatedIntFloat((a, b)->(int)StrictMath.pow(a,1.0/b), (a, b)->StrictMath.pow(a,1.0/b))));
        putData("><", 1060, (self) -> {
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
        putData("!", 860, (self)->null, oneBool((a)->!a));
        putData("?", 860, (self)->null, isTruthy());
        putData("&", 850, (self)->null, differentiatedIntBool((a,b)->a&b, (a, b)->a&&b));
        putData("|", 820, (self)->null, differentiatedIntBool((a,b)->a|b, (a, b)->a||b));
        putData("^", 820, (self)->null, differentiatedIntBool((a,b)->a^b, (a, b)->a^b));
        putData("~&", 850, (self)->null, differentiatedIntBool((a,b)->~(a&b), (a, b)->!(a&&b)));
        putData("~|", 820, (self)->null, differentiatedIntBool((a,b)->~(a|b), (a, b)->!(a||b)));
        putData("~^", 820, (self)->null, differentiatedIntBool((a,b)->~(a^b), (a, b)->a==b));
    }

    /**
     * Comparison operators inhabit the precedence range 500-600
     */
    private static void initComparison() {
        putData("==", 550, (self) -> {
            //TODO
            return null;
        }, Comparison.generalIntFloatCharString(Objects::equals));
        putData("!=", 550, (self) -> {
            //TODO
            return null;
        }, Comparison.generalIntFloatCharString((a,b)-> !Objects.equals(a, b)));
        putData(">", 550, (self) -> {
            //TODO
            return null;
        }, Comparison.generalIntFloatCharString((a,b)-> a.compareTo(b)>0));
        putData("<", 550, (self) -> {
            //TODO
            return null;
        }, Comparison.generalIntFloatCharString((a,b)-> a.compareTo(b)<0));
        putData(">=", 550, (self) -> {
            //TODO
            return null;
        }, Comparison.generalIntFloatCharString((a,b)-> a.compareTo(b)>=0));
        putData("<=", 550, (self) -> {
            //TODO
            return null;
        }, Comparison.generalIntFloatCharString((a,b)-> a.compareTo(b)<=0));
        putData("?=", 555, (self) -> {
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
        putData(".", 430, (self)->null, listChecked(
                (lists)->
                        //TODO not just lists
                        new InterpretList(lists.stream().flatMap((list)->list.getValues().stream()).collect(Collectors.toList()))
                ));
        putData("...", 440, (self)->null, isTruthy());
        putData("in", 820, (self)->null, (self)->new InterpretBool(((InterpretCollectionValue)self.getSecond().interpretValue()).getValues().contains(self.getFirst().interpretValue()))); //TODO fix and clean
        putData("#", 1800, (self)->null, listChecked((list)->new InterpretNumberValue<>(list.get(0).getValues().size())));
    }

    /**
     * Control operators inhabit the precedence range -100-50
     * This range overlaps with some other operators
     */
    private static void initControl() {
        putData("if", -20, IfNode::new);
        putData("repeat", -20, RepeatNode::new);
        putData("while", -20, WhileNode::new);
        putData("for", -20, ForNode::new);
        putData("else", -20, ElseNode::new);
        putData("any", -20, AnyNode::new);
        putData("all", -20, AllNode::new);
        putData("break", -20, (self)->null, (self)->null);
        putData("continue", -20, (self)->null, (self)->null);
    }

    static {
        putData("=", 0, AssignNode::new);
        putData(",", 100, ()->new TupleNode(","));
        putData(";", -1000, ()->new TupleNode(";"));
        putData(":", 1500, 150, ConvertNode::new);
        putData("as", 5, 5, ConvertNode::new);
        putData("@", 5000, (self) -> {
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

    public static Set<String> symbolPrefixes() {
        HashSet<String> delimiters = new HashSet<>(List.of("+", "-", "~", "#", "!", "?", ":", "@", "if", "else", "any", "all", "while", "repeat", "for"));   //TODO
        return delimiters;
    }

    public static Set<String> symbolSuffixes() {
        HashSet<String> delimiters = new HashSet<>(List.of(";"));   //TODO
        return delimiters;
    }

    public static Set<Character> signStartDelimiters() {
        HashSet<Character> delimiters = new HashSet<>(List.of('(', '[', '{'));   //TODO
        return delimiters;
    }

    public static Set<String> symbolControls() {
        HashSet<String> controls = new HashSet<>(List.of("if", "else", "any", "all", "while", "repeat", "for"));   //TODO
        return controls;
    }
    public static Set<String> symbolPrimaryControls() {
        HashSet<String> delimiters = new HashSet<>(List.of("if", "repeat", "while", "for"));   //TODO
        return delimiters;
    }
    public static Set<String> symbolSecondaryControls() {
        HashSet<String> delimiters = new HashSet<>(List.of("else", "any", "all"));   //TODO
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

    public static boolean isBreaking(String nextToken) {
        return true;    //TODO
    }



    int leftPrec, rightPrec;
    private Supplier<Node> constructor;

    private Operator(int leftPrec, int rightPrec, @NonNull Supplier<Node> constructor) {
        this.leftPrec = leftPrec;
        this.rightPrec = rightPrec;
        this.constructor = constructor;
    }

    public Node getOperator() {
        return constructor.get();
    }
}
