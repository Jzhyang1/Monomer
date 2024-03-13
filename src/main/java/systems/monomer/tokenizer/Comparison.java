package systems.monomer.tokenizer;

import systems.monomer.interpreter.*;
import systems.monomer.syntaxtree.operators.GenericOperatorNode;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class Comparison {
    public static Function<GenericOperatorNode, InterpretValue> differentiatedIntFloatCharString(BiFunction<Integer, Integer, Boolean> intCallback, BiFunction<Double, Double, Boolean> floatCallback, BiFunction<Character, Character, Boolean> charCallback, BiFunction<String, String, Boolean> stringCallback) {
        return (self) -> {
            InterpretResult firstr = self.getFirst().interpretValue();
            if(!firstr.isValue()) throw new RuntimeException("First value is not a value");
            InterpretResult secondr = self.getSecond().interpretValue();
            if(!secondr.isValue()) throw new RuntimeException("Second value is not a value");

            InterpretValue first = firstr.asValue(), second = secondr.asValue();

            if(first instanceof InterpretNumber<?> firstNum && second instanceof InterpretNumber<?> secondNum) {
                if (firstNum.getValue() instanceof Integer && secondNum.getValue() instanceof Integer) {
                    return new InterpretBool(intCallback.apply(firstNum.getValue().intValue(), secondNum.getValue().intValue()));
                } else if (firstNum.getValue() instanceof Number && secondNum.getValue() instanceof Number) {
                    return new InterpretBool(floatCallback.apply(firstNum.getValue().doubleValue(), secondNum.getValue().doubleValue()));
                }
            } else if (first instanceof InterpretChar firstChar && second instanceof InterpretChar secondChar) {
                return new InterpretBool(charCallback.apply(firstChar.getValue(), secondChar.getValue()));
            } else if (first instanceof InterpretString firstString && second instanceof InterpretString secondString) {
                return new InterpretBool(stringCallback.apply(firstString.getValue(), secondString.getValue()));
            }
            throw self.syntaxError("Unsupported operation \"" + first + " " + self.getName() + " " + second + "\" with the given types");
        };
    }

    public static Function<GenericOperatorNode, InterpretValue> generalIntFloatCharString(BiFunction<Comparable, Comparable, Boolean> callback) {
        return differentiatedIntFloatCharString(callback::apply, callback::apply, callback::apply, callback::apply);
    }

}
