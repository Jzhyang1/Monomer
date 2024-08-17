package systems.monomer.compiler.output;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import systems.monomer.compiler.operators.CompileOperatorNode;
import systems.monomer.compiler.output.scripts.ArithmeticAssembly;
import systems.monomer.compiler.output.scripts.BitwiseAssembly;
import systems.monomer.compiler.output.scripts.ComparisonAssembly;
import systems.monomer.compiler.output.scripts.implementations.UnixArithmeticAssembly;
import systems.monomer.compiler.output.scripts.implementations.UnixBitwiseAssembly;
import systems.monomer.compiler.output.scripts.implementations.UnixComparisonAssembly;
import systems.monomer.syntaxtree.operators.GenericOperatorNode;
import systems.monomer.types.collection.CollectionType;
import systems.monomer.types.primitive.BoolType;
import systems.monomer.types.primitive.FloatType;
import systems.monomer.types.primitive.IntType;

import java.util.function.BiFunction;
import java.util.function.Function;

@UtilityClass
public final class CompileUtil {
    /**
     * asma: assembly arithmetic commands
     * asmb: assembly bool & bit commands
     * asmc: assembly comparison commands
     */
    @Getter @Setter public static ArithmeticAssembly asma = new UnixArithmeticAssembly();
    @Getter @Setter public static BitwiseAssembly asmb = new UnixBitwiseAssembly();
    @Getter @Setter public static ComparisonAssembly asmc = new UnixComparisonAssembly();


    public static Function<GenericOperatorNode, BiFunction<CompileOperatorNode, CompileOutput, CompileValue>> unBiOp(
            Function<GenericOperatorNode, BiFunction<CompileOperatorNode, CompileOutput, CompileValue>> unaryCallback,
            Function<GenericOperatorNode, BiFunction<CompileOperatorNode, CompileOutput, CompileValue>> binaryCallback) {
        return (self) -> {
            return self.size() == 1 ? unaryCallback.apply(self) : binaryCallback.apply(self);
        };
    }

    public static Function<GenericOperatorNode, BiFunction<CompileOperatorNode, CompileOutput, CompileValue>> intFloatOp(
            BiFunction<CompileOperatorNode, CompileOutput, CompileValue> intCallback,
            BiFunction<CompileOperatorNode, CompileOutput, CompileValue> floatCallback) {
        return (self) -> self.getChildren().stream().allMatch(n -> IntType.INT.typeContains(n.getType())) ? intCallback : floatCallback;
    }

    public static Function<GenericOperatorNode, BiFunction<CompileOperatorNode, CompileOutput, CompileValue>> intFloatUniOp(
            BiFunction<CompileOperatorNode, CompileOutput, CompileValue> intCallback,
            BiFunction<CompileOperatorNode, CompileOutput, CompileValue> floatCallback) {
        return (self) ->  IntType.INT.typeContains(self.getFirst().getType()) ? intCallback : floatCallback;
    }

    public static Function<GenericOperatorNode, BiFunction<CompileOperatorNode, CompileOutput, CompileValue>> intFloatBoolColOp(
            BiFunction<CompileOperatorNode, CompileOutput, CompileValue> intCallback,
            BiFunction<CompileOperatorNode, CompileOutput, CompileValue> floatCallback,
            BiFunction<CompileOperatorNode, CompileOutput, CompileValue> boolCallback,
            BiFunction<CompileOperatorNode, CompileOutput, CompileValue> colCallback
    ) {
        return (self) -> {
            if(self.getChildren().stream().allMatch(n -> n.getType() instanceof CollectionType)) return colCallback;

            if (self.getChildren().stream().allMatch(n -> BoolType.BOOL.typeContains(n.getType()))) {
                return boolCallback;
            } else if (self.getChildren().stream().allMatch(n -> IntType.INT.typeContains(n.getType()))) {
                return intCallback;
            } else {
                return floatCallback;
            }
        };
    }

    public static Function<GenericOperatorNode, BiFunction<CompileOperatorNode, CompileOutput, CompileValue>> intFloatBoolColUniOp(
            BiFunction<CompileOperatorNode, CompileOutput, CompileValue> intCallback,
            BiFunction<CompileOperatorNode, CompileOutput, CompileValue> floatCallback,
            BiFunction<CompileOperatorNode, CompileOutput, CompileValue> boolCallback,
            BiFunction<CompileOperatorNode, CompileOutput, CompileValue> colCallback
    ) {
        return (self) -> {
            if(self.getFirst().getType() instanceof CollectionType) return colCallback;

            if (BoolType.BOOL.typeContains(self.getFirst().getType())) {
                return boolCallback;
            } else if (IntType.INT.typeContains(self.getFirst().getType())) {
                return intCallback;
            } else {
                return floatCallback;
            }
        };
    }

    public static Function<GenericOperatorNode, BiFunction<CompileOperatorNode, CompileOutput, CompileValue>> floatOp(BiFunction<CompileOperatorNode, CompileOutput, CompileValue> floatCallback) {
        return (op) -> {
            assert op.getChildren().stream().allMatch(n -> FloatType.FLOAT.typeContains(n.getType()));
            return floatCallback;
        };
    }

    public static Function<GenericOperatorNode, BiFunction<CompileOperatorNode, CompileOutput, CompileValue>> boolOp(BiFunction<CompileOperatorNode, CompileOutput, CompileValue> boolCallback) {
        return (op) -> {
            assert op.getChildren().stream().allMatch(n -> BoolType.BOOL.typeContains(n.getType()));
            return boolCallback;
        };
    }

    public static Function<GenericOperatorNode, BiFunction<CompileOperatorNode, CompileOutput, CompileValue>> intBoolOp(
            BiFunction<CompileOperatorNode, CompileOutput, CompileValue> intCallback,
            BiFunction<CompileOperatorNode, CompileOutput, CompileValue> boolCallback) {
        return (self) -> {
            if (self.getChildren().stream().allMatch(n -> IntType.INT.typeContains(n.getType()))) {
                return intCallback;
            } else if (self.getChildren().stream().allMatch(n -> BoolType.BOOL.typeContains(n.getType()))) {
                return boolCallback;
            }
            throw self.runtimeError("Unsupported operation \"" + self.getName() + "\" between " + self.getFirst().getType() + " and " + self.getSecond().getType());
        };
    }
}
