package systems.monomer.syntaxTree;

import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.util.Pair;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class OperatorNode extends Node {
    public static class Data {
        private static Map<String, Data> operators = new HashMap<>();

        /**
         * @param leftPrec  a higher number signals a higher precedence
         * @param rightPrec a higher number signals a higher precedence
         */
        private static void putData(String symbol, int leftPrec, int rightPrec, Supplier<Node> constructor) {
            operators.put(symbol, new Data(leftPrec, rightPrec, constructor));
        }

        private static void putData(String symbol, int prec, Supplier<Node> constructor) {
            operators.put(symbol, new Data(prec, prec, constructor));
        }

        private static void putData(String symbol, int prec, Function<GenericOperatorNode, CompileValue> compile, Function<GenericOperatorNode, InterpretValue> interpret) {
            operators.put(symbol, new Data(prec, prec, () -> new GenericOperatorNode(symbol) {{
                setCompile(compile);
                setInterpret(interpret);
            }}));
        }

        private static void putData(String symbol, int leftPrec, int rightPrec, Function<GenericOperatorNode, CompileValue> compile, Function<GenericOperatorNode, InterpretValue> interpret) {
            operators.put(symbol, new Data(leftPrec, rightPrec, () -> new GenericOperatorNode(symbol) {{
                setCompile(compile);
                setInterpret(interpret);
            }}));
        }

        private static void putData(String symbol, int prec, Function<GenericOperatorNode, CompileValue> compile, BiFunction<InterpretValue, InterpretValue, InterpretValue> interpret) {
            operators.put(symbol, new Data(prec, prec, () -> new GenericOperatorNode(symbol) {{
                setCompile(compile);
                setInterpret((self) -> interpret.apply(self.getFirst().interpretValue(), self.getSecond().interpretValue()));
            }}));
        }

        private static void putData(String symbol, int leftPrec, int rightPrec, Function<GenericOperatorNode, CompileValue> compile, BiFunction<InterpretValue, InterpretValue, InterpretValue> interpret) {
            operators.put(symbol, new Data(leftPrec, rightPrec, () -> new GenericOperatorNode(symbol) {{
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
            }, (first, second) -> {
                return null;
            });
            putData("-", 1050, (self) -> {
                //TODO
                return null;
            }, (first, second) -> {
                return null;
            });
            putData("*", 1055, (self) -> {
                //TODO
                return null;
            }, (first, second) -> {
                return null;
            });
            putData("/", 1055, (self) -> {
                //TODO
                return null;
            }, (first, second) -> {
                return null;
            });
            putData("%", 1055, (self) -> {
                //TODO
                return null;
            }, (first, second) -> {
                return null;
            });


            putData("||", 1065, (self) -> {
                //TODO
                return null;
            }, (first, second) -> {
                return null;
            });
            putData("><", 1060, (self) -> {
                //TODO
                return null;
            }, (first, second) -> {
                return null;
            });

            putData("**", 1075, (self) -> {
                //TODO
                return null;
            }, (first, second) -> {
                return null;
            });
            putData("*/", 1075, (self) -> {
                //TODO
                return null;
            }, (first, second) -> {
                return null;
            });
        }

        /**
         * Bitwise operators inhabit the precedence range 700-900
         */
        private static void initBitwise() {

        }

        /**
         * Comparison operators inhabit the precedence range 500-600
         */
        private static void initComparison() {
            putData("==", 550, (self) -> {
                //TODO
                return null;
            }, (first, second) -> {
                return null;
            });
            putData("!=", 550, (self) -> {
                //TODO
                return null;
            }, (first, second) -> {
                return null;
            });
            putData(">", 550, (self) -> {
                //TODO
                return null;
            }, (first, second) -> {
                return null;
            });
            putData("<", 550, (self) -> {
                //TODO
                return null;
            }, (first, second) -> {
                return null;
            });
            putData(">=", 550, (self) -> {
                //TODO
                return null;
            }, (first, second) -> {
                return null;
            });
            putData("<=", 550, (self) -> {
                //TODO
                return null;
            }, (first, second) -> {
                return null;
            });
        }

        /**
         * Control operators inhabit the precedence range -100-50
         * This range overlaps with some other operators
         */
        private static void initControl() {
            putData("if", -20, (self) -> {
                //TODO
                return null;
            }, (first, second) -> {
                return null;
            });
            putData("repeat", -20, (self) -> {
                //TODO
                return null;
            }, (first, second) -> {
                return null;
            });
            putData("while", -20, (self) -> {
                //TODO
                return null;
            }, (first, second) -> {
                return null;
            });
            putData("for", -20, (self) -> {
                //TODO
                return null;
            }, (first, second) -> {
                return null;
            });
            putData("else", -20, (self) -> {
                //TODO
                return null;
            }, (first, second) -> {
                return null;
            });
            putData("any", -20, (self) -> {
                //TODO
                return null;
            }, (first, second) -> {
                return null;
            });
            putData("all", -20, (self) -> {
                //TODO
                return null;
            }, (first, second) -> {
                return null;
            });
        }

        static {
            putData("=", 0, AssignOperatorNode::new);
            putData(",", 100, TupleNode::new);
            putData(";", -1000, TupleNode::new);
            putData(":", 20, 10, TupleNode::new);

            initComparison();
            initBitwise();
            initArithmetic();
            initControl();
        }

        int leftPrec, rightPrec;

        private Data(int leftPrec, int rightPrec, Supplier<Node> constructor) {
            this.leftPrec = leftPrec;
            this.rightPrec = rightPrec;
        }
    }

    public static Set<String> symbolOperators() {  //TODO
        return Data.operators.keySet();
    }

    public static Set<String> symbolPrefixes() {
        HashSet<String> delimiters = new HashSet<>(List.of("+", "-", "~", "#", "!", ":", "if", "else", "any", "all", "while", "repeat", "for"));   //TODO
        return delimiters;
    }

    public static Set<String> symbolControls() {
        HashSet<String> controls = new HashSet<>(List.of("if", "else", "any", "all", "while", "repeat", "for"));   //TODO
        return controls;
    }

    public static Set<String> symbolSuffixes() {
        HashSet<String> delimiters = new HashSet<>(List.of(";"));   //TODO
        return delimiters;
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
        Data dat = Data.operators.get(op);
        return new Pair<>(dat.leftPrec, dat.rightPrec);
    }

    public static boolean isChained(String a, String b) {
        List<Set<String>> chains = List.of(
                new TreeSet<>(List.of(";")),
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

    public OperatorNode(String name) {
        super(name);
    }


    public Node getFirst() {
        return get(0);
    }

    public Node getSecond() {
        return get(1);
    }

    public Usage getUsage() {
        return Usage.OPERATOR;
    }

    //TODO probably make a ChainedOperatorNode and move this there
    private List<String> names = new ArrayList<>();
    public void addName(String name) {
        if(names.isEmpty()) super.addName(name);
        names.add(name);
    }
}
