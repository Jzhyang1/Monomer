package systems.monomer.tokenizer;

import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.interpreter.*;
import systems.monomer.syntaxtree.operators.GenericOperatorNode;

import java.util.function.BiFunction;
import java.util.function.Function;

import static systems.monomer.errorhandling.ErrorBlock.programError;

public final class Comparison {
    public static BiFunction<InterpretValue, InterpretValue, Boolean> differentiatedIntFloatCharString(BiFunction<Integer, Integer, Boolean> intCallback, BiFunction<Double, Double, Boolean> floatCallback, BiFunction<Character, Character, Boolean> charCallback, BiFunction<String, String, Boolean> stringCallback) {
        return (first, second) -> {
            if(first instanceof InterpretNumber<?> firstNum && second instanceof InterpretNumber<?> secondNum) {
                if (firstNum.getValue() instanceof Integer && secondNum.getValue() instanceof Integer) {
                    return intCallback.apply(firstNum.getValue().intValue(), secondNum.getValue().intValue());
                } else if (firstNum.getValue() instanceof Number && secondNum.getValue() instanceof Number) {
                    return floatCallback.apply(firstNum.getValue().doubleValue(), secondNum.getValue().doubleValue());
                }
            } else if (first instanceof InterpretChar firstChar && second instanceof InterpretChar secondChar) {
                return charCallback.apply(firstChar.getValue(), secondChar.getValue());
            } else if (first instanceof InterpretString firstString && second instanceof InterpretString secondString) {
                return stringCallback.apply(firstString.getValue(), secondString.getValue());
            }
            throw programError("Failed in Comparison.java");
        };
    }

    public static Function<GenericOperatorNode, InterpretValue> binary(BiFunction<Integer, Integer, Boolean> intCallback, BiFunction<Double, Double, Boolean> floatCallback, BiFunction<Character, Character, Boolean> charCallback, BiFunction<String, String, Boolean> stringCallback) {
        BiFunction<InterpretValue, InterpretValue, Boolean> callback = differentiatedIntFloatCharString(intCallback, floatCallback, charCallback, stringCallback);

        return (self) -> {
            InterpretResult firstr = self.getFirst().interpretValue();
            if(!firstr.isValue()) throw self.runtimeError("First value is not a value");
            InterpretResult secondr = self.getSecond().interpretValue();
            if(!secondr.isValue()) throw self.runtimeError("Second value is not a value");

            InterpretValue first = firstr.asValue(), second = secondr.asValue();

            try{
                return new InterpretBool(callback.apply(first, second));
            } catch (ErrorBlock.ProgramErrorException e) {
                throw self.runtimeError("Unsupported operation \"" + first + " " + self.getName() + " " + second + "\" with the given types");
            }
        };
    }

    public static Function<GenericOperatorNode, InterpretValue> chained(BiFunction<Integer, Integer, Boolean> intCallback, BiFunction<Double, Double, Boolean> floatCallback, BiFunction<Character, Character, Boolean> charCallback, BiFunction<String, String, Boolean> stringCallback) {
        BiFunction<InterpretValue, InterpretValue, Boolean> callback = differentiatedIntFloatCharString(intCallback, floatCallback, charCallback, stringCallback);

        return (self) -> {
            int stop = self.size() - 1;
            InterpretResult previous = self.get(0).interpretValue();
            if(!previous.isValue()) throw self.runtimeError("First value is not a value");
            InterpretValue previousValue = previous.asValue();
            boolean previousBool = true;

            for(int i = 0; i < stop && previousBool; ++i) {
                InterpretResult next = self.get(i + 1).interpretValue();
                if(!next.isValue()) throw self.runtimeError("Value at " + i + " is not a value");

                InterpretValue nextValue = next.asValue();

                try {
                    previousBool = callback.apply(previousValue, nextValue);
                } catch (ErrorBlock.ProgramErrorException e) {
                    throw self.runtimeError("Unsupported operation \"... " + self.getName() + " " + nextValue + "\" with the given types");
                }
                previousValue = nextValue;
            }

            return new InterpretBool(previousBool);
        };
    }

    public static Function<GenericOperatorNode, InterpretValue> binary(BiFunction<Comparable, Comparable, Boolean> callback) {
        return binary(callback::apply, callback::apply, callback::apply, callback::apply);
    }

    public static Function<GenericOperatorNode, InterpretValue> chained(BiFunction<Comparable, Comparable, Boolean> callback) {
        return chained(callback::apply, callback::apply, callback::apply, callback::apply);
    }
}
