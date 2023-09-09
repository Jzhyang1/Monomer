package systems.monomer.syntaxtree.operators;

import systems.monomer.interpreter.InterpretNumber;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.NumberType;
import systems.monomer.types.Type;

import java.util.function.BiFunction;
import java.util.function.Function;

enum Arithmetic {
    ;

    static Function<GenericOperatorNode, InterpretResult> numericalChecked(BiFunction<InterpretNumber<? extends Number>, InterpretNumber<? extends Number>, ? extends InterpretResult> callback) {
        return (self) -> {
            InterpretResult firstr = self.getFirst().interpretValue();
            if(!firstr.isValue()) throw new RuntimeException("First value is not a value");
            InterpretResult secondr = self.getSecond().interpretValue();
            if(!secondr.isValue()) throw new RuntimeException("Second value is not a value");

            InterpretValue first = firstr.asValue(), second = secondr.asValue();

            if (first instanceof InterpretNumber<? extends Number> firstNum && second instanceof InterpretNumber<? extends Number> secondNum) {
                return callback.apply(firstNum, secondNum);
            }
            self.throwError("Unsupported operation \"" + self.getName() + "\" with non-numeric values");
            return null;
        };
    }

    static BiFunction<InterpretNumber<? extends Number>, InterpretNumber<? extends Number>, ? extends InterpretResult> differentiatedIntFloat(BiFunction<Integer, Integer, Integer> intCallback, BiFunction<Double, Double, Double> floatCallback) {
        return (first, second) -> {
            if (first.getValue() instanceof Integer && second.getValue() instanceof Integer) {
                return new InterpretNumber<>(intCallback.apply(first.getValue().intValue(), second.getValue().intValue()));
            } else {
                return new InterpretNumber<>(floatCallback.apply(first.getValue().doubleValue(), second.getValue().doubleValue()));
            }
        };
    }

    static BiFunction<InterpretNumber<? extends Number>, InterpretNumber<? extends Number>, ? extends InterpretResult> alwaysFloat(BiFunction<Double, Double, Double> floatCallback) {
        return (first, second) -> new InterpretNumber<>(floatCallback.apply(first.getValue().doubleValue(), second.getValue().doubleValue()));
    }

    static Type typeFor(GenericOperatorNode self){
        if(self.getType() != null) return self.getType();
        else {
            Node first = self.getFirst();
            Node second = self.getSecond();
            if(first.getType() instanceof NumberType<?> firstNum && second.getType() instanceof NumberType<?> secondNum) {
                if(firstNum.getValue() instanceof Integer && secondNum.getValue() instanceof Integer) {
                    return NumberType.INTEGER;
                } else {
                    return NumberType.FLOAT;
                }
            }
            else if(first.getType() instanceof NumberType<?> firstNum) {
                return firstNum;
            }
            else if(second.getType() instanceof NumberType<?> secondNum) {
                return secondNum;
            }
            else {
                self.throwError("Unsupported operation \"" + self.getName() + "\" with non-numeric values: " + first.getType() + " and " + second.getType());
                return null;
            }
        }
    }
}
