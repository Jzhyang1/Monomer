package systems.monomer.tokenizer;

import lombok.experimental.UtilityClass;
import systems.monomer.compiler.Assembly.Instruction;
import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.Assembly.Register;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.operators.CompileOperatorNode;
import systems.monomer.interpreter.*;
import systems.monomer.interpreter.operators.InterpretOperatorNode;
import systems.monomer.interpreter.values.InterpretBool;
import systems.monomer.interpreter.values.InterpretCollection;
import systems.monomer.interpreter.values.InterpretNumber;
import systems.monomer.types.*;

import java.util.function.BiFunction;
import java.util.function.Function;

@UtilityClass
public final class Bitwise {
    Function<InterpretOperatorNode, InterpretValue> differentiatedIntBool(BiFunction<Integer, Integer, Integer> intCallback, BiFunction<Boolean, Boolean, Boolean> boolCallback) {
        return (self) -> {
            InterpretResult firstr = self.getFirstInterpretNode().interpretValue();
            if (!firstr.isValue()) throw self.runtimeError("First value is not a value");
            InterpretResult secondr = self.getSecondInterpretNode().interpretValue();
            if (!secondr.isValue()) throw self.runtimeError("Second value is not a value");

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
            throw self.runtimeError("Unsupported operation \"" + self.getName() + "\" between " + first + " and " + second);
        };
    }

    Function<InterpretOperatorNode, ? extends InterpretResult> oneBool(Function<Boolean, Boolean> callback) {
        return (self) -> {
            InterpretResult firstr = self.getFirstInterpretNode().interpretValue();
            if (!firstr.isValue()) throw self.runtimeError("First value is not a value");
            InterpretValue first = firstr.asValue();

            if (BoolType.BOOL.typeContains(first)) {
                return new InterpretBool(callback.apply(first.getValue()));
            }
            throw self.runtimeError("Unsupported operation \"" + self.getName() + "\" on " + first);
        };
    }

    Function<InterpretOperatorNode, ? extends InterpretResult> oneAny(Function<InterpretValue, Boolean> callback) {
        return (self) -> {
            InterpretResult firstr = self.getFirstInterpretNode().interpretValue();
            if (!firstr.isValue()) throw self.runtimeError("First value is not a value");
            InterpretValue first = firstr.asValue();

            return new InterpretBool(callback.apply(first));
        };
    }

    Function<InterpretOperatorNode, ? extends InterpretResult> isTruthy() {
        return (self) -> {
            InterpretResult firstr = self.getFirstInterpretNode().interpretValue();
            if (!firstr.isValue()) throw self.runtimeError("First value is not a value");
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
                throw self.runtimeError("Expected a boolean-y value, got " + first);
            }
        };
    }

    Operand compileBitwiseBinary(AssemblyFile file, CompileOperatorNode self, Instruction instruction) {
        Operand first = self.getFirstCompileNode().compileValue(file);
        //TODO optimize by checking for if possible to store in registers w/o being overwritten
        //TODO check all compileValue functions to make sure that all volatile data is stored
        //TODO all bools will be stored in _X registers, and unused values will be popped before returning
        file.push(first);
        Operand second = self.getSecondCompileNode().compileValue(file);
        file.mov(second, Register.AX.toOperand())
                .pop(Register.BX.toOperand());

        if (self.getFirst().getType().equals(BoolType.BOOL) && self.getSecond().getType().equals(BoolType.BOOL)) {
            file.add(instruction, first, second);
//        } else if(self.getFirst().getType().equals(NumberType.INTEGER) && self.getSecond().getType().equals(NumberType.INTEGER)) {
//            file.add(Instruction.)
        } else {
            throw self.runtimeError("Unable to compile bitwise operation between " + self.getFirst().getType() + " and " + self.getSecond().getType());
        }

        return Register.RAX.toOperand();
    }
}
