package systems.monomer.tokenizer;

import lombok.experimental.UtilityClass;
import systems.monomer.interpreter.InterpretNumber;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.operators.GenericOperatorNode;
import systems.monomer.types.NumberType;
import systems.monomer.types.Type;

import java.util.function.BiFunction;
import java.util.function.Function;

import static systems.monomer.types.AnyType.ANY;

@UtilityClass
public class Arithmetic {
    Function<GenericOperatorNode, InterpretResult> numericalChecked(
            Function<InterpretNumber<? extends Number>, ? extends InterpretResult> unaryCallback,
            BiFunction<InterpretNumber<? extends Number>, InterpretNumber<? extends Number>, ? extends InterpretResult> binaryCallback
    ) {
        return (self) -> {
            if(self.size() == 1) return numericalChecked(unaryCallback).apply(self);
            else return numericalChecked(binaryCallback).apply(self);
        };
    }

    /**
     * binary variant
     * @param callback
     * @return
     */
    Function<GenericOperatorNode, InterpretResult> numericalChecked(BiFunction<InterpretNumber<? extends Number>, InterpretNumber<? extends Number>, ? extends InterpretResult> callback) {
        return (self) -> {
            InterpretResult firstr = self.getFirst().interpretValue();
            if (!firstr.isValue()) throw self.runtimeError("First value is not a value");
            InterpretResult secondr = self.getSecond().interpretValue();
            if (!secondr.isValue()) throw self.runtimeError("Second value is not a value");

            InterpretValue first = firstr.asValue(), second = secondr.asValue();

            if (first instanceof InterpretNumber<? extends Number> firstNum && second instanceof InterpretNumber<? extends Number> secondNum) {
                return callback.apply(firstNum, secondNum);
            }
            throw self.runtimeError("Unsupported operation \"" + self.getName() + "\" with non-numeric values");
        };
    }

    /**
     * unary variant
     * @param callback
     * @return
     */
    Function<GenericOperatorNode, InterpretResult> numericalChecked(Function<InterpretNumber<? extends Number>, ? extends InterpretResult> callback) {
        return (self) -> {
            InterpretResult firstr = self.getFirst().interpretValue();
            if (!firstr.isValue()) throw self.runtimeError("First value is not a value");

            InterpretValue first = firstr.asValue();

            if (first instanceof InterpretNumber<? extends Number> firstNum) {
                return callback.apply(firstNum);
            }
            throw self.runtimeError("Unsupported operation \"" + self.getName() + "\" with non-numeric values");
        };
    }


    BiFunction<InterpretNumber<? extends Number>, InterpretNumber<? extends Number>, ? extends InterpretResult> differentiatedIntFloat(BiFunction<Integer, Integer, Integer> intCallback, BiFunction<Double, Double, Double> floatCallback) {
        return (first, second) -> {
            if (first.getValue() instanceof Integer && second.getValue() instanceof Integer) {
                return new InterpretNumber<>(intCallback.apply(first.getValue().intValue(), second.getValue().intValue()));
            } else {
                return new InterpretNumber<>(floatCallback.apply(first.getValue().doubleValue(), second.getValue().doubleValue()));
            }
        };
    }

    Function<InterpretNumber<? extends Number>, ? extends InterpretResult> differentiatedIntFloat(Function<Integer, Integer> intCallback, Function<Double, Double> floatCallback) {
        return (first) -> {
            if (first.getValue() instanceof Integer) {
                return new InterpretNumber<>(intCallback.apply(first.getValue().intValue()));
            } else {
                return new InterpretNumber<>(floatCallback.apply(first.getValue().doubleValue()));
            }
        };
    }

    BiFunction<InterpretNumber<? extends Number>, InterpretNumber<? extends Number>, ? extends InterpretResult> alwaysFloat(BiFunction<Double, Double, Double> floatCallback) {
        return (first, second) -> new InterpretNumber<>(floatCallback.apply(first.getValue().doubleValue(), second.getValue().doubleValue()));
    }

    Type typeFor(GenericOperatorNode self) {
        if (self.getType() != ANY) return self.getType();
        else if(self.size() == 1) {
            Node first = self.getFirst();
            if (first.getType() instanceof NumberType<?> firstNum) {
                return firstNum;
            } else {
                throw self.syntaxError("Unsupported operation \"" + self.getName() + "\" with non-numeric values: " + first.getType());
            }
        }
        else {
            Node first = self.getFirst();
            Node second = self.getSecond();
            if (first.getType() instanceof NumberType<?> firstNum && second.getType() instanceof NumberType<?> secondNum) {
                if (firstNum.getValue() instanceof Integer && secondNum.getValue() instanceof Integer) {
                    return NumberType.INTEGER;
                } else {
                    return NumberType.FLOAT;
                }
            } else if (first.getType() instanceof NumberType<?> firstNum) {
                return firstNum;
            } else if (second.getType() instanceof NumberType<?> secondNum) {
                return secondNum;
            } else {
                throw self.syntaxError("Unsupported operation \"" + self.getName() + "\" with non-numeric values: " + first.getType() + " and " + second.getType());
            }
        }
    }
}
