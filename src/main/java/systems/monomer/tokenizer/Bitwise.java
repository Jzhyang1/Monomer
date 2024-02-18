package systems.monomer.tokenizer;

import lombok.experimental.UtilityClass;
import systems.monomer.compiler.Assembly.Instruction;
import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.Assembly.Register;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.interpreter.*;
import systems.monomer.syntaxtree.operators.GenericOperatorNode;
import systems.monomer.types.*;

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

            if (NumberType.INTEGER.typeContains(first) && NumberType.INTEGER.typeContains(second)) {
                return new InterpretNumber<>(intCallback.apply(
                        (first).<Number>getValue().intValue(),
                        (first).<Number>getValue().intValue()
                ));
            } else if (BoolType.BOOL.typeContains(first) && BoolType.BOOL.typeContains(second)) {
                return new InterpretBool(boolCallback.apply(
                        first.getValue(),
                        second.getValue()
                ));
            }
            throw self.syntaxError("Unsupported operation \"" + self.getName() + "\" between " + first + " and " + second);
        };
    }

    Function<GenericOperatorNode, ? extends InterpretResult> oneBool(Function<Boolean, Boolean> callback) {
        return (self) -> {
            InterpretResult firstr = self.getFirst().interpretValue();
            if (!firstr.isValue()) throw new RuntimeException("First value is not a value");
            InterpretValue first = firstr.asValue();

            if (BoolType.BOOL.typeContains(first)) {
                return new InterpretBool(callback.apply(first.getValue()));
            }
            throw self.syntaxError("Unsupported operation \"" + self.getName() + "\" on " + first);
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

            if (BoolType.BOOL.typeContains(first)) {
                return first;
            } else if (NumberType.INTEGER.typeContains(first)) {
                return new InterpretBool(first.<Number>getValue().doubleValue() != 0.0);
            } else if (StringType.STRING.typeContains(first)) {
                return new InterpretBool(!first.<String>getValue().isEmpty());
            } else if (CharType.CHAR.typeContains(first)) {
                return new InterpretBool(first.<Character>getValue() != '\0');
            } else if (CollectionType.COLLECTION.typeContains(first)) {
                return new InterpretBool(((InterpretCollection)first).size() != 0);
            } else {
                throw new Error("Expected a boolean-y value, got " + first);
            }
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
