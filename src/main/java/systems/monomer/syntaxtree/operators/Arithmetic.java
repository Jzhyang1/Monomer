package systems.monomer.syntaxtree.operators;

import systems.monomer.interpreter.InterpretNumberValue;
import systems.monomer.interpreter.InterpretValue;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class Arithmetic {
    public static Function<GenericOperatorNode, InterpretValue> numericalChecked(BiFunction<InterpretNumberValue<? extends Number>, InterpretNumberValue<? extends Number>, InterpretValue> callback) {
        return (self) -> {
            InterpretValue first = self.getFirst().interpretValue();
            InterpretValue second = self.getSecond().interpretValue();
            if (first instanceof InterpretNumberValue<? extends Number> firstNum && second instanceof InterpretNumberValue<? extends Number> secondNum) {
                return callback.apply(firstNum, secondNum);
            }
            self.throwError("Unsupported operation \"" + self.getName() + "\" with non-numeric values");
            return null;
        };
    }

    public static BiFunction<InterpretNumberValue<? extends Number>, InterpretNumberValue<? extends Number>, InterpretValue> differentiatedIntFloat(BiFunction<Integer, Integer, Integer> intCallback, BiFunction<Double, Double, Double> floatCallback) {
        return (first, second) -> {
            if (first.getValue() instanceof Integer && second.getValue() instanceof Integer) {
                return new InterpretNumberValue<>(intCallback.apply(first.getValue().intValue(), second.getValue().intValue()));
            } else {
                return new InterpretNumberValue<>(floatCallback.apply(first.getValue().doubleValue(), second.getValue().doubleValue()));
            }
        };
    }

    public static BiFunction<InterpretNumberValue<? extends Number>, InterpretNumberValue<? extends Number>, InterpretValue> alwaysFloat(BiFunction<Double, Double, Double> floatCallback) {
        return (first, second) -> new InterpretNumberValue<>(floatCallback.apply(first.getValue().doubleValue(), second.getValue().doubleValue()));
    }
}
