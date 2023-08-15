package systems.monomer.syntaxtree.operators;

import systems.monomer.interpreter.*;
import systems.monomer.syntaxtree.Node;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class Comparison {
    public static Function<GenericOperatorNode, InterpretValue> differentiatedIntFloatCharString(BiFunction<Integer, Integer, Boolean> intCallback, BiFunction<Double, Double, Boolean> floatCallback, BiFunction<Character, Character, Boolean> charCallback, BiFunction<String, String, Boolean> stringCallback) {
        return (self) -> {
            InterpretValue first = self.getFirst().interpretValue(), second = self.getSecond().interpretValue();

            if(first instanceof InterpretNumberValue<?> firstNum && second instanceof InterpretNumberValue<?> secondNum) {
                if (firstNum.getValue() instanceof Integer && secondNum.getValue() instanceof Integer) {
                    return new InterpretBool(intCallback.apply(firstNum.getValue().intValue(), secondNum.getValue().intValue()));
                } else if (firstNum.getValue() instanceof Number && secondNum.getValue() instanceof Number) {
                    return new InterpretBool(floatCallback.apply(firstNum.getValue().doubleValue(), secondNum.getValue().doubleValue()));
                }
            } else if (first instanceof InterpretCharValue firstChar && second instanceof InterpretCharValue secondChar) {
                return new InterpretBool(charCallback.apply(firstChar.getValue(), secondChar.getValue()));
            } else if (first instanceof InterpretStringValue firstString && second instanceof InterpretStringValue secondString) {
                return new InterpretBool(stringCallback.apply(firstString.getValue(), secondString.getValue()));
            }
            self.throwError("Unsupported operation \"" + first + " " + self.getName() + " " + second + "\" with the given types");
            return null;
        };
    }

    public static Function<GenericOperatorNode, InterpretValue> generalIntFloatCharString(BiFunction<Comparable, Comparable, Boolean> callback) {
        return differentiatedIntFloatCharString(callback::apply, callback::apply, callback::apply, callback::apply);
    }

}
