package systems.monomer.tokenizer;

import lombok.NonNull;
import systems.monomer.compiler.output.CompileOutput;
import systems.monomer.compiler.output.CompileUtil;
import systems.monomer.compiler.output.CompileValue;
import systems.monomer.execution.Constants;

import static systems.monomer.compiler.output.CompileUtil.asma;
import static systems.monomer.compiler.output.CompileUtil.asmb;
import static systems.monomer.compiler.output.CompileUtil.asmc;

import systems.monomer.compiler.operators.CompileOperatorNode;
import systems.monomer.interpreter.*;
import systems.monomer.interpreter.execution.InterpretUtil;
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

import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static systems.monomer.execution.Handler.init;
import static systems.monomer.types.pseudo.AnyType.ANY;

//TODO class is too large
public final class Operator {
    @SuppressWarnings("StaticCollection")
    private static final Map<String, Operator> operators = new HashMap<>();
    public static final int NONE = 0;
    public static final int BINARY = 0b1, PREFIX = 0b10, SUFFIX = 0b100, CHAINED = 0b1000, ASSIGN = 0b10000;
    public static final int CONTROL = PREFIX | CHAINED | 0b100000;
    public static final int PRIMARY_CONTROL = CONTROL | 0b10000000, SECONDARY_CONTROL = CONTROL | 0b100000000;
    public static final int WORD = 0b1000000000;


    private static int fillInfo(int info, String symbol) {
        if (Constants.isIdentifierChar(symbol.charAt(0))) {
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

    private static void putData(String symbol, int prec, int info,
                                Function<GenericOperatorNode, BiFunction<CompileOperatorNode, CompileOutput, CompileValue>> compile,
                                Function<GenericOperatorNode, Function<Iterator<InterpretValue>, ? extends InterpretResult>> interpret,
                                Function<OperatorNode, Type> type) {
        operators.put(symbol, new Operator(fillInfo(info, symbol), prec, prec, ()->init.genericOperatorNode(symbol, type, compile, interpret)));
    }

    /**
     * Arithmetic operators inhabit the precedence range 1000-1200
     */
    @SuppressWarnings("FeatureEnvy")
    private static void initArithmetic() {
        putData("+", 1050, PREFIX | BINARY, 
                CompileUtil.unBiOp(CompileUtil.intFloatUniOp(asma::posi, asma::posf), CompileUtil.intFloatOp(asma::addi, asma::addf)),
                InterpretUtil.unBi(InterpretUtil.intFloatUniOp((a)->+a, (b)->+b), InterpretUtil.intFloatOp((a, b) -> a + b, (a, b) -> a + b)),
                Operator::arithmeticTypeFor);
        putData("-", 1050, PREFIX | BINARY,
                CompileUtil.unBiOp(CompileUtil.intFloatUniOp(asma::negi, asma::negf), CompileUtil.intFloatOp(asma::subi, asma::subf)),
                InterpretUtil.unBi(InterpretUtil.intFloatUniOp((a)->-a, (b)->-b), InterpretUtil.intFloatOp((a, b) -> a - b, (a, b) -> a - b)),
                Operator::arithmeticTypeFor);
        putData("*", 1055, BINARY,
                CompileUtil.intFloatOp(asma::muli, asma::mulf),
                InterpretUtil.intFloatOp((a, b) -> a * b, (a, b) -> a * b),
                Operator::arithmeticTypeFor);
        putData("/", 1055, BINARY,
                CompileUtil.intFloatOp(asma::divi, asma::divf),
                InterpretUtil.intFloatOp((a, b) -> a / b, (a, b) -> a / b),
                Operator::arithmeticTypeFor);
        putData("%", 1055, BINARY,
                CompileUtil.intFloatOp(asma::modi, asma::modf),
                InterpretUtil.intFloatOp((a, b) -> b == 0 ? 0 : a % b, (a, b) -> b == 0 ? 0 : a % b),
                Operator::arithmeticTypeFor);
        putData("||", 1065, BINARY,
                CompileUtil.intFloatOp(asma::plli, asma::pllf),
                InterpretUtil.intFloatOp((a, b) -> a * b / (a + b), (a, b) -> a * b / (a + b)),
                Operator::arithmeticTypeFor);
        putData("**", 1075, BINARY,
                CompileUtil.intFloatOp(asma::powi, asma::powf),
                //TODO instead of using .pow for ints, use a non-built-in helper function in InterpretUtil
                InterpretUtil.intFloatOp((a, b) -> (int) StrictMath.pow(a, b), StrictMath::pow),
                Operator::arithmeticTypeFor);
        putData("*/", 1075, BINARY,
                CompileUtil.intFloatOp(asma::rooti, asma::rootf),
                InterpretUtil.intFloatOp((a, b) -> (int) StrictMath.pow(a, 1.0 / b), (a, b) -> StrictMath.pow(a, 1.0 / b)),
                Operator::arithmeticTypeFor);
        //TODO
//        putData("><", 1060, BINARY, null, null, null);
    }

    /**
     * Bitwise operators inhabit the precedence range 700-900
     */
    @SuppressWarnings("FeatureEnvy")
    private static void initBitwise() {
        //TODO replace (self) -> BoolType.BOOL with a named function that also handles non-bool
        putData("!", 860, PREFIX,
                CompileUtil.intBoolOp(asmb::noti, asmb::notb),
                InterpretUtil.boolOp((a) -> !a),
                (self) -> BoolType.BOOL);
        putData("?", 860, PREFIX,
                CompileUtil.intFloatBoolColUniOp(asmb::isi, asmb::isf, asmb::isb, asmb::isl),
                InterpretUtil.truthyOp(),
                (self) -> BoolType.BOOL);
        putData("&", 850, BINARY,
                CompileUtil.intBoolOp(asmb::andi, asmb::andb),
                InterpretUtil.intBoolOp((a, b) -> a & b, (a, b) -> a && b),
                (self) -> BoolType.BOOL);
        putData("|", 820, BINARY,
                CompileUtil.intBoolOp(asmb::ori, asmb::orb),
                InterpretUtil.intBoolOp((a, b) -> a | b, (a, b) -> a || b),
                (self) -> BoolType.BOOL);
        putData("^", 820, BINARY,
                CompileUtil.intBoolOp(asmb::xori, asmb::xorb),
                InterpretUtil.intBoolOp((a, b) -> a ^ b, (a, b) -> a ^ b),
                (self) -> BoolType.BOOL);
        putData("~&", 850, BINARY,
                CompileUtil.intBoolOp(asmb::nandi, asmb::nandb),
                InterpretUtil.intBoolOp((a, b) -> ~(a & b), (a, b) -> !(a && b)),
                (self) -> BoolType.BOOL);
        putData("~|", 820, BINARY,
                CompileUtil.intBoolOp(asmb::nori, asmb::norb),
                InterpretUtil.intBoolOp((a, b) -> ~(a | b), (a, b) -> !(a || b)),
                (self) -> BoolType.BOOL);
        putData("~^", 820, BINARY,
                CompileUtil.intBoolOp(asmb::nxori, asmb::nxorb),
                InterpretUtil.intBoolOp((a, b) -> ~(a ^ b), (a, b) -> a == b),
                (self) -> BoolType.BOOL);
    }

    /**
     * Comparison operators inhabit the precedence range 500-600
     */
    @SuppressWarnings({"FeatureEnvy"})
    private static void initComparison() {
        putData("==", 550, BINARY | CHAINED,
                CompileUtil.intFloatBoolColOp(asmc::eqi, asmc::eqf, asmc::eqb, asmc::eql),
                InterpretUtil.cmpOp((a, b) -> a == b, (a, b) -> a == b, (a, b) -> a == b, (a, b) -> a.equals(b)),
                (self) -> BoolType.BOOL);
        putData("!=", 550, BINARY | CHAINED,
                CompileUtil.intFloatBoolColOp(asmc::neqi, asmc::neqf, asmc::neqb, asmc::neql),
                InterpretUtil.cmpOp((a, b) -> a != b, (a, b) -> a != b, (a, b) -> a != b, (a, b) -> !a.equals(b)),
                (self) -> BoolType.BOOL);
        putData(">", 550, BINARY | CHAINED,
                CompileUtil.intFloatBoolColOp(asmc::gti, asmc::gtf, asmc::gtb, asmc::gtl),
                InterpretUtil.cmpOp((a, b) -> a > b, (a, b) -> a > b, (a, b) -> a && !b, (a, b) -> false /*TODO*/),
                (self) -> BoolType.BOOL);
        putData("<", 550, BINARY | CHAINED,
                CompileUtil.intFloatBoolColOp(asmc::lti, asmc::ltf, asmc::ltb, asmc::ltl),
                InterpretUtil.cmpOp((a, b) -> a < b, (a, b) -> a < b, (a, b) -> !a && b, (a, b) -> false /*TODO*/),
                (self) -> BoolType.BOOL);
        putData(">=", 550, BINARY | CHAINED,
                CompileUtil.intFloatBoolColOp(asmc::gteqi, asmc::gteqf, asmc::gteqb, asmc::gteql),
                InterpretUtil.cmpOp((a, b) -> a >= b, (a, b) -> a >= b, (a, b) -> a || !b, (a, b) -> false /*TODO*/),
                (self) -> BoolType.BOOL);
        putData("<=", 550, BINARY | CHAINED,
                CompileUtil.intFloatBoolColOp(asmc::lteqi, asmc::lteqf, asmc::lteqb, asmc::lteql),
                InterpretUtil.cmpOp((a, b) -> a <= b, (a, b) -> a <= b, (a, b) -> !a || b, (a, b) -> false /*TODO*/),
                (self) -> BoolType.BOOL);
        putData("?=", 555, BINARY,
                CompileUtil.intFloatBoolColUniOp(asmc::cmpi, asmc::cmpf, asmc::cmpb, asmc::cmpl),
                null /*TODO*/,
                (self) -> null);
    }

    /**
     * List operators inhabit the precedence range 400-500 with 1 exception
     */
    @SuppressWarnings({"FeatureEnvy"})
    private static void initList() {
        putData(".", 430, BINARY | CHAINED,
                null, //TODO
                InterpretUtil.colOp((col1, col2) -> {
                            col1.addAll(col2);
                            return col1;
                        }) ,
                (self) -> self.getFirst().getType()); //TODO fix
        putData("...", 440, PREFIX | BINARY,
                null,   //TODO
                InterpretUtil.unBi(InterpretUtil.spreadOp(), InterpretUtil.rangeOp()),
                (self) -> self.size() == 1 ? new SequenceType(((CollectionType)self.getFirst().getType()).getElementType()) : new InterpretRanges(self.getFirst().getType())
        );    //TODO fix and clean
        putData("in", 420, BINARY,
                null,
                InterpretUtil.inOp(),
                (self) -> BoolType.BOOL);
        putData("#", 1800, PREFIX,
                null,
                InterpretUtil.sizeOp(),
                (self) -> IntType.INT
        );
    }

    /**
     * Control operators inhabit the precedence range -100-50
     * This range overlaps with some other operators
     */
    @SuppressWarnings({"FeatureEnvy"})
    private static void initControl() {
        putData("if",       -20, PRIMARY_CONTROL,   init::ifNode);
        putData("repeat",   -20, PRIMARY_CONTROL,   init::repeatNode);
        putData("while",    -20, PRIMARY_CONTROL,   init::whileNode);
        putData("for",      -20, PRIMARY_CONTROL,   init::forNode);
        putData("else",     -20, SECONDARY_CONTROL, init::elseNode);
        putData("any",      -20, SECONDARY_CONTROL, init::anyNode);
        putData("all",      -20, SECONDARY_CONTROL, init::allNode);
        putData("break",    -10, PREFIX | SUFFIX,
                null,
                InterpretUtil.breakingOp(), //self contains the name of the breaking operator
                (self)->null);
        putData("continue", -10, PREFIX | SUFFIX,
                null,
                InterpretUtil.breakingOp(), //self contains the name of the breaking operator
                (self)->null);
        putData("return", -10, PREFIX | SUFFIX,
                null,
                InterpretUtil.breakingOp(), //self contains the name of the breaking operator
                (self)->null);
    }

    static {
        putData("=", 0, BINARY | CHAINED | ASSIGN, init::assignNode);
//        putData("+=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("-=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("*=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("/=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("%=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("&=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("|=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("^=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
        putData(",", 100, BINARY | CHAINED | SUFFIX, init::tupleNode);
        putData(";", -1000, BINARY | CHAINED | SUFFIX, init::linesNode);
        putData(":", 1500, 150, BINARY, init::assertTypeNode);
        putData("as", 5, BINARY, init::convertNode);
        putData("to", 5, BINARY, init::castNode);
        putData("@", 5000, PREFIX, null, (self) -> (iter) -> {
            InterpretValue first = iter.next();
            try {
                Constants.getOut().write(first.valueString().getBytes());
                Constants.getOut().write('\n');
                Constants.getOut().flush();
            } catch (IOException e) {
                throw self.runtimeError(e.getMessage());
            }
            return first;
        }, (self) -> self.getFirst().getType());
        putData("with", -5, PREFIX, init::withNode);
        putData("then", -5, PREFIX, init::thenNode);

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
        return Collections.unmodifiableSet(startingSymobolOperatorCharacterSet);
    }

    public static boolean isToken(String symbol, int info) {
        return (operators.get(symbol).info & info) == info;
    }
    public static boolean isOperator(String symbol) {
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
