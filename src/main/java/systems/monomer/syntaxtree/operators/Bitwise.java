package systems.monomer.syntaxtree.operators;

import systems.monomer.interpreter.*;
import systems.monomer.syntaxtree.Node;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Bitwise {
    public static Function<GenericOperatorNode, InterpretValue> differentiatedIntBool(BiFunction<Integer, Integer, Integer> intCallback, BiFunction<Boolean, Boolean, Boolean> boolCallback) {
        return (self) -> {
            InterpretValue first = self.getFirst().interpretValue();
            InterpretValue second = self.getSecond().interpretValue();
            if (first instanceof InterpretNumberValue<? extends Number> firstNum && second instanceof InterpretNumberValue<? extends Number> secondNum) {
                if(firstNum.getValue() instanceof Integer && secondNum.getValue() instanceof Integer)
                    return new InterpretNumberValue<>(intCallback.apply(firstNum.getValue().intValue(), secondNum.getValue().intValue()));
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
            else if(first instanceof InterpretNumberValue<? extends Number> firstNum)
                return new InterpretBool(firstNum.getValue().doubleValue() != 0.0);
            else if(first instanceof InterpretStringValue firstString)
                return new InterpretBool(firstString.getValue().length() != 0);
            else if(first instanceof InterpretCharValue firstChar)
                return new InterpretBool(firstChar.getValue() != '\0');
            else if(first instanceof InterpretCollectionValue firstCollection)
                return new InterpretBool(firstCollection.getValues().size() != 0);
            else
                return new InterpretBool(first.getFields().isEmpty());
        };
    }
}
