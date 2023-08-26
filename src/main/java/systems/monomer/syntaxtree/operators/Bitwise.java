package systems.monomer.syntaxtree.operators;

import systems.monomer.interpreter.*;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class Bitwise {
    public static Function<GenericOperatorNode, InterpretValue> differentiatedIntBool(BiFunction<Integer, Integer, Integer> intCallback, BiFunction<Boolean, Boolean, Boolean> boolCallback) {
        return (self) -> {
            InterpretValue first = self.getFirst().interpretValue();
            InterpretValue second = self.getSecond().interpretValue();
            if (first instanceof InterpretNumber<? extends Number> firstNum && second instanceof InterpretNumber<? extends Number> secondNum) {
                if(firstNum.getValue() instanceof Integer && secondNum.getValue() instanceof Integer)
                    return new InterpretNumber<>(intCallback.apply(firstNum.getValue().intValue(), secondNum.getValue().intValue()));
            } else if(first instanceof InterpretBool firstBool && second instanceof InterpretBool secondBool){
                return new InterpretBool(boolCallback.apply(firstBool.getValue(), secondBool.getValue()));
            }
            self.throwError("Unsupported operation \"" + self.getName() + "\" between " + first + " and " + second);
            return null;
        };
    }

    public static Function<GenericOperatorNode, InterpretValue> oneBool(Function<Boolean, Boolean> callback) {
        return (self) -> {
            InterpretValue first = self.getFirst().interpretValue();
            if(first instanceof InterpretBool firstBool){
                return new InterpretBool(callback.apply(firstBool.getValue()));
            }
            self.throwError("Unsupported operation \"" + self.getName() + "\" on " + first);
            return null;
        };
    }
    public static Function<GenericOperatorNode, InterpretValue> oneAny(Function<InterpretValue, Boolean> callback) {
        return (self) -> {
            InterpretValue first = self.getFirst().interpretValue();
            return new InterpretBool(callback.apply(first));
        };
    }

    public static Function<GenericOperatorNode, InterpretValue> isTruthy() {
        return (self) -> {
            InterpretValue first = self.getFirst().interpretValue();
            if(first instanceof InterpretBool firstBool)
                return firstBool;
            else if(first instanceof InterpretNumber<? extends Number> firstNum)
                return new InterpretBool(firstNum.getValue().doubleValue() != 0.0);
            else if(first instanceof InterpretString firstString)
                return new InterpretBool(firstString.getValue().length() != 0);
            else if(first instanceof InterpretChar firstChar)
                return new InterpretBool(firstChar.getValue() != '\0');
            else if(first instanceof InterpretCollection firstCollection)
                return new InterpretBool(firstCollection.getValues().size() != 0);
            else
                throw new Error("Expected a boolean-y value, got " + first);
        };
    }
}
