package systems.monomer.tokenizer;

import systems.monomer.execution.Constants;

import systems.monomer.execution.Initialized;
import systems.monomer.interpreter.values.*;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.operators.*;
import systems.monomer.types.*;
import systems.monomer.types.collection.CollectionType;
import systems.monomer.types.collection.SequenceType;
import systems.monomer.types.primitive.BoolType;
import systems.monomer.types.primitive.FloatType;
import systems.monomer.types.primitive.IntType;
import systems.monomer.util.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static systems.monomer.types.pseudo.AnyType.ANY;

public final class Operators extends Initialized<Operators> {
    public static final int NONE = 0;
    public static final int BINARY = 0b1, PREFIX = 0b10, SUFFIX = 0b100, CHAINED = 0b1000, ASSIGN = 0b10000;
    public static final int CONTROL = PREFIX | CHAINED | 0b100000;
    public static final int PRIMARY_CONTROL = CONTROL | 0b10000000, SECONDARY_CONTROL = CONTROL | 0b100000000;
    public static final int WORD = 0b1000000000;

    private final Map<String, OperatorEntry> operators = new HashMap<>();

    private static int fillInfo(int info, String symbol) {
        if (Constants.isIdentifierChar(symbol.charAt(0))) {
            info |= WORD;
        }
        return info;
    }

    public Operators() {
        putData("=", 0, BINARY | CHAINED | ASSIGN, env::assignNode);
//        putData("+=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("-=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("*=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("/=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("%=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("&=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("|=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("^=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
        putData(",", 100, BINARY | CHAINED | SUFFIX, env::tupleNode);
        putData(";", -1000, BINARY | CHAINED | SUFFIX, env::linesNode);
        putData(":", 1500, 150, BINARY, env::assertTypeNode);
        putData("as", 5, BINARY, env::convertNode);
        putData("to", 5, BINARY, env::castNode);
        putData("@", 5000, PREFIX, (self) -> self.getFirst().getType());
        putData("with", -5, PREFIX, env::withNode);
        putData("then", -5, PREFIX, env::thenNode);

        initComparison();
        initBitwise();
        initArithmetic();
        initList();
        initControl();

        Set<String> tempSymbols = operators.entrySet().stream().filter((entry) -> (entry.getValue().info & WORD) != WORD).map(Map.Entry::getKey).collect(Collectors.toSet());

        symbolOperatorSet = tempSymbols;
        wordOperatorSet = operators.entrySet().stream().filter((entry) -> (entry.getValue().info & WORD) == WORD).map(Map.Entry::getKey).collect(Collectors.toSet());
        startingSymobolOperatorCharacterSet = tempSymbols.stream().map((s) -> s.charAt(0)).collect(Collectors.toSet());
    }

    /**
     * @param leftPrec  a higher number signals a higher precedence
     * @param rightPrec a higher number signals a higher precedence
     */
    private void putData(String symbol, int leftPrec, int rightPrec, int info, Supplier<Node> constructor) {
        operators.put(symbol, new OperatorEntry(fillInfo(info, symbol), leftPrec, rightPrec, constructor));
    }

    private void putData(String symbol, int prec, int info, Supplier<Node> constructor) {
        operators.put(symbol, new OperatorEntry(fillInfo(info, symbol), prec, prec, constructor));
    }

    private void putData(String symbol, int prec, int info, Function<OperatorNode, Type> type) {
        operators.put(symbol, new OperatorEntry(fillInfo(info, symbol), prec, prec, ()->env.genericOperatorNode(symbol, type)));
    }

    /**
     * Arithmetic operators inhabit the precedence range 1000-1200
     */
    private void initArithmetic() {
        putData("+", 1050, PREFIX | BINARY,
                Operators::arithmeticTypeFor);
        putData("-", 1050, PREFIX | BINARY,
                Operators::arithmeticTypeFor);
        putData("*", 1055, BINARY,
                Operators::arithmeticTypeFor);
        putData("/", 1055, BINARY,
                Operators::arithmeticTypeFor);
        putData("%", 1055, BINARY,
                Operators::arithmeticTypeFor);
        putData("||", 1065, BINARY,
                Operators::arithmeticTypeFor);
        putData("**", 1075, BINARY,
                //TODO instead of using .pow for ints, use a non-built-in helper function in InterpretUtil
                Operators::arithmeticTypeFor);
        putData("*/", 1075, BINARY,
                Operators::arithmeticTypeFor);
    }

    /**
     * Bitwise operators inhabit the precedence range 700-900
     */
    private void initBitwise() {
        //TODO replace (self) -> BoolType.BOOL with a named function that also handles non-bool
        putData("!", 860, PREFIX,
                (self) -> BoolType.BOOL);
        putData("?", 860, PREFIX,
                (self) -> BoolType.BOOL);
        putData("&", 850, BINARY,
                (self) -> BoolType.BOOL);
        putData("|", 820, BINARY,
                (self) -> BoolType.BOOL);
        putData("^", 820, BINARY,
                (self) -> BoolType.BOOL);
        putData("~&", 850, BINARY,
                (self) -> BoolType.BOOL);
        putData("~|", 820, BINARY,
                (self) -> BoolType.BOOL);
        putData("~^", 820, BINARY,
                (self) -> BoolType.BOOL);
    }

    /**
     * Comparison operators inhabit the precedence range 500-600
     */
    private void initComparison() {
        putData("==", 550, BINARY | CHAINED,
                (self) -> BoolType.BOOL);
        putData("!=", 550, BINARY | CHAINED,
                (self) -> BoolType.BOOL);
        putData(">", 550, BINARY | CHAINED,
                (self) -> BoolType.BOOL);
        putData("<", 550, BINARY | CHAINED,
                (self) -> BoolType.BOOL);
        putData(">=", 550, BINARY | CHAINED,
                (self) -> BoolType.BOOL);
        putData("<=", 550, BINARY | CHAINED,
                (self) -> BoolType.BOOL);
        putData("?=", 555, BINARY,
                /*TODO*/
                (self) -> null);
    }

    /**
     * List operators inhabit the precedence range 400-500 with 1 exception
     */
    @SuppressWarnings({"FeatureEnvy"})
    private void initList() {
        putData(".", 430, BINARY | CHAINED,
                //TODO
                (self) -> self.getFirst().getType()); //TODO fix
        putData("...", 440, PREFIX | BINARY,
                //TODO
                (self) -> self.size() == 1 ? new SequenceType(((CollectionType)self.getFirst().getType()).getElementType()) : new InterpretRanges(self.getFirst().getType())
        );    //TODO fix and clean
        putData("in", 420, BINARY,
                (self) -> BoolType.BOOL);
        putData("#", 1800, PREFIX,
                (self) -> IntType.INT
        );
    }

    /**
     * Control operators inhabit the precedence range -100-50
     * This range overlaps with some other operators
     */
    private void initControl() {
        putData("if",       -20, PRIMARY_CONTROL,   env::ifNode);
        putData("repeat",   -20, PRIMARY_CONTROL,   env::repeatNode);
        putData("while",    -20, PRIMARY_CONTROL,   env::whileNode);
        putData("for",      -20, PRIMARY_CONTROL,   env::forNode);
        putData("else",     -20, SECONDARY_CONTROL, env::elseNode);
        putData("any",      -20, SECONDARY_CONTROL, env::anyNode);
        putData("all",      -20, SECONDARY_CONTROL, env::allNode);
        putData("break",    -10, PREFIX | SUFFIX,
                //self contains the name of the breaking operator
                (self)->null);
        putData("continue", -10, PREFIX | SUFFIX,
                //self contains the name of the breaking operator
                (self)->null);
        putData("return", -10, PREFIX | SUFFIX,
                //self contains the name of the breaking operator
                (self)->null);
    }


    //TODO ==========vvvvvv this doesn't belong here vvvvvv==========

    //TODO make into "mostInclusiveTypeFor"
    static Type arithmeticTypeFor(OperatorNode self) {
        if (self.getType() != ANY) return self.getType();
        else if(self.size() == 1) {
            Type firstType = self.getFirst().getType();

            assert IntType.INT.typeContains(firstType) || FloatType.FLOAT.typeContains(firstType);
            return firstType;
        }
        else {
            Type firstType = self.getFirst().getType();
            Type secondType = self.getSecond().getType();

            if (IntType.INT.typeContains(firstType) && IntType.INT.typeContains(secondType)) {
                return IntType.INT;
            } else {
                return FloatType.FLOAT;
            }
        }
    }



    public Node getOperator(String name) {
        return operators.get(name).getOperator().with(env);
    }

    private final Set<String> symbolOperatorSet;

    public Set<String> symbolOperators() {
        return symbolOperatorSet;
    }

    private final Set<String> wordOperatorSet;

    public Set<String> wordOperators() {
        return wordOperatorSet;
    }

    /**
     * optimization for the lexer
     */
    private final Set<Character> startingSymobolOperatorCharacterSet;

    public Set<Character> startingSymbolOperatorCharacters() {
        return Collections.unmodifiableSet(startingSymobolOperatorCharacterSet);
    }

    public boolean isToken(String symbol, int info) {
        return (operators.get(symbol).info & info) == info;
    }
    public boolean isOperator(String symbol) {
        return operators.containsKey(symbol);
    }

    private static final Set<Character> startDelimiters = new HashSet<>(List.of('(', '[', '{'));
    public static Set<Character> signStartDelimiters() {
        return Collections.unmodifiableSet(startDelimiters);
    }

    private static final Set<Character> endDelimiters = new HashSet<>(List.of(')', ']', '}'));
    public static Set<Character> signEndDelimiters() {
        return Collections.unmodifiableSet(endDelimiters);
    }

    public Pair<Integer, Integer> precedence(String op) {
        OperatorEntry dat = operators.get(op);
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
}
