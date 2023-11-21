package systems.monomer.syntaxtree.operators;

import lombok.experimental.UtilityClass;
import systems.monomer.compiler.Assembly.Instruction;
import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.Assembly.Register;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.interpreter.*;
import systems.monomer.types.BoolType;

import java.util.function.BiFunction;
import java.util.function.Function;

@UtilityClass
public final class Bitwise {
    Function<GenericOperatorNode, InterpretValue> differentiatedIntBool(BiFunction<Integer, Integer, Integer> intCallback, BiFunction<Boolean, Boolean, Boolean> boolCallback) {
        return (self) -> {
            InterpretResult firstr = self.getFirst().interpretValue();
            if (!firstr.isValue()) throw new RuntimeException("First value is not a value");
            InterpretResult secondr = self.getSecond().interpretValue();
            if (!secondr.isValue()) throw new RuntimeException("Second value is not a value");

            InterpretValue first = firstr.asValue(), second = secondr.asValue();

            if (first instanceof InterpretNumber<? extends Number> firstNum && second instanceof InterpretNumber<? extends Number> secondNum) {
                if (firstNum.getValue() instanceof Integer && secondNum.getValue() instanceof Integer)
                    return new InterpretNumber<>(intCallback.apply(firstNum.getValue().intValue(), secondNum.getValue().intValue()));
            } else if (first instanceof InterpretBool firstBool && second instanceof InterpretBool secondBool) {
                return new InterpretBool(boolCallback.apply(firstBool.getValue(), secondBool.getValue()));
            }
            self.throwError("Unsupported operation \"" + self.getName() + "\" between " + first + " and " + second);
            return null;
        };
    }

    Function<GenericOperatorNode, ? extends InterpretResult> oneBool(Function<Boolean, Boolean> callback) {
        return (self) -> {
            InterpretResult firstr = self.getFirst().interpretValue();
            if (!firstr.isValue()) throw new RuntimeException("First value is not a value");
            InterpretValue first = firstr.asValue();

            if (first instanceof InterpretBool firstBool) {
                return new InterpretBool(callback.apply(firstBool.getValue()));
            }
            self.throwError("Unsupported operation \"" + self.getName() + "\" on " + first);
            return null;
        };
    }

    Function<GenericOperatorNode, ? extends InterpretResult> oneAny(Function<InterpretValue, Boolean> callback) {
        return (self) -> {
            InterpretResult firstr = self.getFirst().interpretValue();
            if (!firstr.isValue()) throw new RuntimeException("First value is not a value");
            InterpretValue first = firstr.asValue();

            return new InterpretBool(callback.apply(first));
        };
    }

    Function<GenericOperatorNode, ? extends InterpretResult> isTruthy() {
        return (self) -> {
            InterpretResult firstr = self.getFirst().interpretValue();
            if (!firstr.isValue()) throw new RuntimeException("First value is not a value");
            InterpretValue first = firstr.asValue();

            if (first instanceof InterpretBool firstBool)
                return firstBool;
            else if (first instanceof InterpretNumber<? extends Number> firstNum)
                return new InterpretBool(firstNum.getValue().doubleValue() != 0.0);
            else if (first instanceof InterpretString firstString)
                return new InterpretBool(!firstString.getValue().isEmpty());
            else if (first instanceof InterpretChar firstChar)
                return new InterpretBool(firstChar.getValue() != '\0');
            else if (first instanceof InterpretCollection firstCollection)
                return new InterpretBool(!firstCollection.getValues().isEmpty());
            else
                throw new Error("Expected a boolean-y value, got " + first);
        };
    }

    Operand compileBitwiseBinary(AssemblyFile file, GenericOperatorNode self, Instruction instruction) {
        Operand first = self.getFirst().compileValue(file);
        //TODO optimize by checking for if possible to store in registers w/o being overwritten
        //TODO check all compileValue functions to make sure that all volatile data is stored
        //TODO all bools will be stored in _X registers, and unused values will be popped before returning
        file.push(first);
        Operand second = self.getSecond().compileValue(file);
        file.mov(second, Register.AX.toOperand())
                .pop(Register.BX.toOperand());

        if (self.getFirst().getType().equals(BoolType.BOOL) && self.getSecond().getType().equals(BoolType.BOOL)) {
            file.add(instruction, first, second);
//        } else if(self.getFirst().getType().equals(NumberType.INTEGER) && self.getSecond().getType().equals(NumberType.INTEGER)) {
//            file.add(Instruction.)
        } else {
            throw new Error("Unable to compile bitwise operation between " + self.getFirst().getType() + " and " + self.getSecond().getType());
        }

        return Register.RAX.toOperand();
    }
}
