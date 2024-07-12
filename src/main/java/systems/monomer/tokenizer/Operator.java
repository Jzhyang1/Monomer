package systems.monomer.tokenizer;

import lombok.NonNull;
import systems.monomer.execution.Constants;

import static systems.monomer.compiler.ArithmeticAssembly.compileNumericalBinary;
import static systems.monomer.compiler.assembly.Instruction.*;

import systems.monomer.compiler.ArithmeticAssembly;
import systems.monomer.compiler.assembly.Operand;

import static systems.monomer.compiler.assembly.Register.*;

import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.operators.CompileOperatorNode;
import systems.monomer.interpreter.*;
import systems.monomer.interpreter.operators.InterpretOperatorNode;
import systems.monomer.interpreter.values.*;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.operators.*;
import systems.monomer.types.*;
import systems.monomer.types.plural.CollectionType;
import systems.monomer.types.plural.SequenceType;
import systems.monomer.types.primative.BoolType;
import systems.monomer.util.Pair;

import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static systems.monomer.tokenizer.Arithmetic.*;
import static systems.monomer.tokenizer.Bitwise.*;
import static systems.monomer.tokenizer.Lists.*;

public final class Operator {
    @SuppressWarnings("StaticCollection")
    private static final Map<String, Operator> operators = new HashMap<>();
    private static final int NONE = 0;
    private static final int BINARY = 0b1, PREFIX = 0b10, SUFFIX = 0b100, CHAINED = 0b1000, ASSIGN = 0b10000;
    private static final int CONTROL = PREFIX | CHAINED | 0b100000;
    private static final int PRIMARY_CONTROL = CONTROL | 0b10000000, SECONDARY_CONTROL = CONTROL | 0b100000000;
    private static final int WORD = 0b1000000000;


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
                                BiFunction<CompileOperatorNode, AssemblyFile, Operand> compile,
                                Function<InterpretOperatorNode, ? extends InterpretResult> interpret,
                                Function<OperatorNode, Type> type) {
        operators.put(symbol, new Operator(fillInfo(info, symbol), prec, prec, ()->Node.init.genericOperatorNode(symbol, type, compile, interpret)));
    }

    /**
     * Arithmetic operators inhabit the precedence range 1000-1200
     */
    @SuppressWarnings("FeatureEnvy")
    private static void initArithmetic() {
        putData("+", 1050, PREFIX | BINARY, ArithmeticAssembly::addOrPos, numericalChecked(differentiatedIntFloat((a)->+a, (b)->+b), differentiatedIntFloat((a, b) -> a + b, (a, b) -> a + b)), Arithmetic::typeFor);
        putData("-", 1050, PREFIX | BINARY, ArithmeticAssembly::subOrNeg, numericalChecked(differentiatedIntFloat((a)->-a, (b)->-b), differentiatedIntFloat((a, b) -> a - b, (a, b) -> a - b)), Arithmetic::typeFor);
        putData("*", 1055, BINARY, (self, file) -> {
            return compileNumericalBinary(file, self, IMUL, FMUL);
        }, numericalChecked(differentiatedIntFloat((a, b) -> a * b, (a, b) -> a * b)), Arithmetic::typeFor);
        putData("/", 1055, BINARY, (self, file) -> {
            //TODO optimize paired div and remainder
            return compileNumericalBinary(file, self, IDIV, FDIV);
        }, numericalChecked(differentiatedIntFloat((a, b) -> a / b, (a, b) -> a / b)), Arithmetic::typeFor);
        putData("%", 1055, BINARY, (self, file) -> {
            return compileNumericalBinary(file, self, IMOD, FMOD);
        }, numericalChecked(differentiatedIntFloat((a, b) -> b == 0 ? 0 : a % b, (a, b) -> b == 0 ? 0 : a % b)), Arithmetic::typeFor);
        putData("||", 1065, BINARY, (self, file) -> {
            compileNumericalBinary(file, self, () -> {
                file.add(MOV, RBX.toOperand(), RAX.toOperand())
                        .add(IMUL, RDX.toOperand(), RAX.toOperand())
                        .add(IADD, RDX.toOperand(), RBX.toOperand())
                        .add(IDIV, RBX.toOperand(), RAX.toOperand());
            }, () -> {
                file.add(MOV, RBX.toOperand(), RAX.toOperand())
                        .add(FMUL, RDX.toOperand(), RAX.toOperand())
                        .add(FADD, RDX.toOperand(), RBX.toOperand())
                        .add(FDIV, RBX.toOperand(), RAX.toOperand());
            });

            return RAX.toOperand();
        }, numericalChecked(alwaysFloat((a, b) -> a * b / (a + b))), Arithmetic::typeFor);
        putData("**", 1075, BINARY, (self, file) -> {
            //TODO
            return null;
        }, numericalChecked(differentiatedIntFloat((a, b) -> (int) StrictMath.pow(a, b), StrictMath::pow)), Arithmetic::typeFor);
        putData("*/", 1075, BINARY, (self, file) -> {
            //TODO
            return null;
        }, numericalChecked(differentiatedIntFloat((a, b) -> (int) StrictMath.pow(a, 1.0 / b), (a, b) -> StrictMath.pow(a, 1.0 / b))), Arithmetic::typeFor);
        putData("><", 1060, BINARY, (self, file) -> {
            //TODO
            return null;
        }, (self) -> {
            //TODO
            return null;
        }, (self) -> null);
    }

    /**
     * Bitwise operators inhabit the precedence range 700-900
     */
    @SuppressWarnings("FeatureEnvy")
    private static void initBitwise() {
        //TODO replace (self) -> BoolType.BOOL with a named function that also handles non-bool
        putData("!", 860, PREFIX, (self, file) -> {
            Operand first = self.getFirstCompileNode().compileValue(file);
            file.add(MOV, first, AX.toOperand())
                    .add(NOT, null, AX.toOperand());
            return AX.toOperand();
        }, oneBool((a) -> !a), (self) -> BoolType.BOOL);
        putData("?", 860, PREFIX, (self, file) -> {
            Operand first = self.getFirstCompileNode().compileValue(file);
            file.add(MOV, first, AX.toOperand());
            return AX.toOperand();
        }, isTruthy(), (self) -> BoolType.BOOL);
        putData("&", 850, BINARY, (self, file) -> {
            return compileBitwiseBinary(file, self, AND);
        }, differentiatedIntBool((a, b) -> a & b, (a, b) -> a && b), (self) -> BoolType.BOOL);
        putData("|", 820, BINARY, (self, file) -> {
            return compileBitwiseBinary(file, self, OR);
        }, differentiatedIntBool((a, b) -> a | b, (a, b) -> a || b), (self) -> BoolType.BOOL);
        putData("^", 820, BINARY, (self, file) -> {
            return compileBitwiseBinary(file, self, XOR);
        }, differentiatedIntBool((a, b) -> a ^ b, (a, b) -> a ^ b), (self) -> BoolType.BOOL);
        putData("~&", 850, BINARY, (self, file) -> {
            return null;    //TODO
        }, differentiatedIntBool((a, b) -> ~(a & b), (a, b) -> !(a && b)), (self) -> BoolType.BOOL);
        putData("~|", 820, BINARY, (self, file) -> {
            return null; //todo
        }, differentiatedIntBool((a, b) -> ~(a | b), (a, b) -> !(a || b)), (self) -> BoolType.BOOL);
        putData("~^", 820, BINARY, (self, file) -> {
            return null; //todo
        }, differentiatedIntBool((a, b) -> ~(a ^ b), (a, b) -> a == b), (self) -> BoolType.BOOL);
    }

    /**
     * Comparison operators inhabit the precedence range 500-600
     */
    @SuppressWarnings({"FeatureEnvy"})
    private static void initComparison() {
        putData("==", 550, BINARY | CHAINED, (self, file) -> {
            Operand first = self.getFirstCompileNode().compileValue(file);
            file.push(RDX.toOperand())
                    .mov(first, RDX.toOperand());
            Operand second = self.getSecondCompileNode().compileValue(file);
            file.mov(second, RAX.toOperand());
            //TODO optimize with CMP
            file.add(ISUB, RAX.toOperand(), RDX.toOperand())
                    .add(NOT, RAX.toOperand(), null)
                    .pop(RDX.toOperand());
            return RAX.toOperand();
        }, Comparison.chained(Objects::equals), (self) -> BoolType.BOOL);
        putData("!=", 550, BINARY | CHAINED, (self, file) -> {
            Operand first = self.getFirstCompileNode().compileValue(file);
            file.push(RDX.toOperand())
                    .mov(first, RDX.toOperand());
            Operand second = self.getSecondCompileNode().compileValue(file);
            file.mov(second, RAX.toOperand());
            //TODO optimize with CMP
            file.add(ISUB, RAX.toOperand(), RDX.toOperand())
                    .pop(RDX.toOperand());
            return RAX.toOperand();
        }, Comparison.chained((a, b) -> !Objects.equals(a, b)), (self) -> BoolType.BOOL);
        putData(">", 550, BINARY | CHAINED, (self, file) -> {
            //TODO
            return null;
        }, Comparison.chained((a, b) -> a.compareTo(b) > 0), (self) -> BoolType.BOOL);
        putData("<", 550, BINARY | CHAINED, (self, file) -> {
            //TODO
            return null;
        }, Comparison.chained((a, b) -> a.compareTo(b) < 0), (self) -> BoolType.BOOL);
        putData(">=", 550, BINARY | CHAINED, (self, file) -> {
            //TODO
            return null;
        }, Comparison.chained((a, b) -> a.compareTo(b) >= 0), (self) -> BoolType.BOOL);
        putData("<=", 550, BINARY | CHAINED, (self, file) -> {
            //TODO
            return null;
        }, Comparison.chained((a, b) -> a.compareTo(b) <= 0), (self) -> BoolType.BOOL);
        putData("?=", 555, BINARY, (self, file) -> {
            //TODO
            return null;
        }, (self) -> {
            return null;
        }, (self) -> null);
    }

    /**
     * List operators inhabit the precedence range 400-500 with 1 exception
     */
    @SuppressWarnings({"FeatureEnvy"})
    private static void initList() {
        putData(".", 430, BINARY | CHAINED, (self, file) -> null, listStringChecked(
                        (lists) ->
                                //TODO not just lists
                                new InterpretList(lists.stream().flatMap((list) -> list.getValues().stream()).collect(Collectors.toList())),
                        (strs) -> new InterpretString(strs.stream().map((str) -> str.getValue()).collect(Collectors.joining()))),
                (self) -> self.getFirst().getType()); //TODO fix
        putData("...", 440, PREFIX | BINARY,
                (self, file) -> null,   //TODO
                (self) -> self.size() == 1 ? new InterpretSequence(((InterpretCollection)self.getFirstInterpretNode().interpretValue().asValue()).getValues()) : new InterpretRanges(self.getFirstInterpretNode().interpretValue().asValue(), self.getSecondInterpretNode().interpretValue().asValue(), new InterpretNumber<>(1)),
                (self) -> self.size() == 1 ? new SequenceType(((CollectionType)self.getFirst().getType()).getElementType()) : new InterpretRanges(self.getFirst().getType())
        );    //TODO fix and clean
        putData("in", 420, BINARY,
                (self, file) -> null,
                binaryCollectionChecked(false, true, (first, second) -> new InterpretBool(((InterpretCollection) second).getValues().contains(first))),
                (self) -> BoolType.BOOL);
        putData("#", 1800, PREFIX, (self, file) -> null, listStringChecked(
                        (list) -> new InterpretNumber<>(list.get(0).size()),
                        (strs) -> new InterpretNumber<>(strs.get(0).getValue().length())),
                (self) -> self.getFirst().getType() //TODO fix
        );
    }

    /**
     * Control operators inhabit the precedence range -100-50
     * This range overlaps with some other operators
     */
    @SuppressWarnings({"FeatureEnvy"})
    private static void initControl() {
        putData("if",       -20, PRIMARY_CONTROL,   Node.init::ifNode);
        putData("repeat",   -20, PRIMARY_CONTROL,   Node.init::repeatNode);
        putData("while",    -20, PRIMARY_CONTROL,   Node.init::whileNode);
        putData("for",      -20, PRIMARY_CONTROL,   Node.init::forNode);
        putData("else",     -20, SECONDARY_CONTROL, Node.init::elseNode);
        putData("any",      -20, SECONDARY_CONTROL, Node.init::anyNode);
        putData("all",      -20, SECONDARY_CONTROL, Node.init::allNode);
        putData("break",    -10, PREFIX | SUFFIX,
                (self, file) -> null,
                (self) -> new InterpretBreaking("break",
                        self.size() == 0 ?
                                InterpretTuple.EMPTY :
                                self.getFirstInterpretNode().interpretValue().asValue()),
                (self)->null);
        putData("continue", -10, PREFIX | SUFFIX,
                (self, file) -> null,
                (self) -> new InterpretBreaking("continue",
                        self.size() == 0 ?
                                InterpretTuple.EMPTY :
                                self.getFirstInterpretNode().interpretValue().asValue()),
                (self)->null);
        putData("return", -10, PREFIX | SUFFIX,
                (self, file) -> null,
                (self) -> new InterpretBreaking("return",
                        self.size() == 0 ?
                                InterpretTuple.EMPTY :
                                self.getFirstInterpretNode().interpretValue().asValue()),
                (self)->null);
    }

    static {
        putData("=", 0, BINARY | CHAINED | ASSIGN, Node.init::assignNode);
//        putData("+=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("-=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("*=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("/=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("%=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("&=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("|=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
//        putData("^=", 0,  BINARY | CHAINED | ASSIGN, AssignNode::new);
        putData(",", 100, BINARY | CHAINED | SUFFIX, Node.init::tupleNode);
        putData(";", -1000, BINARY | CHAINED | SUFFIX, Node.init::linesNode);
        putData(":", 1500, 150, BINARY, Node.init::assertTypeNode);
        putData("as", 5, BINARY, Node.init::convertNode);
        putData("to", 5, BINARY, Node.init::castNode);
        putData("@", 5000, PREFIX, (self, file) -> {
            //TODO
            return null;
        }, (self) -> {
            InterpretValue first = self.getFirstInterpretNode().interpretValue().asValue();
            try {
                Constants.getOut().write(first.valueString().getBytes());
                Constants.getOut().write('\n');
                Constants.getOut().flush();
            } catch (IOException e) {
                throw self.runtimeError(e.getMessage());
            }
            return first;
        }, (self) -> self.getFirst().getType());
        putData("with", -5, PREFIX, Node.init::withNode);
        putData("then", -5, PREFIX, Node.init::thenNode);

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
        return operators.containsKey(symbol) && ((operators.get(symbol).info & PRIMARY_CONTROL) == PRIMARY_CONTROL);
    }

    public static boolean isSecondaryControl(String symbol) {
        return operators.containsKey(symbol) &&  ((operators.get(symbol).info & SECONDARY_CONTROL) == SECONDARY_CONTROL);
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
    //TODO make it possible to sort operators that have the same symbol but differ by prefix/suffix/binary

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
