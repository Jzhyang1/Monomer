package systems.monomer.syntaxtree.operators;

import lombok.experimental.UtilityClass;
import systems.monomer.compiler.Assembly.Instruction;
import systems.monomer.compiler.Assembly.Operand;

import static systems.monomer.compiler.Assembly.Register.*;
import static systems.monomer.compiler.Assembly.Instruction.*;

import systems.monomer.compiler.AssemblyFile;
import systems.monomer.interpreter.InterpretNumber;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.NumberType;
import systems.monomer.types.Type;

import java.util.function.BiFunction;
import java.util.function.Function;

import static systems.monomer.types.AnyType.ANY;

@UtilityClass
class Arithmetic {

    Function<GenericOperatorNode, InterpretResult> numericalChecked(BiFunction<InterpretNumber<? extends Number>, InterpretNumber<? extends Number>, ? extends InterpretResult> callback) {
        return (self) -> {
            InterpretResult firstr = self.getFirst().interpretValue();
            if (!firstr.isValue()) throw new RuntimeException("First value is not a value");
            InterpretResult secondr = self.getSecond().interpretValue();
            if (!secondr.isValue()) throw new RuntimeException("Second value is not a value");

            InterpretValue first = firstr.asValue(), second = secondr.asValue();

            if (first instanceof InterpretNumber<? extends Number> firstNum && second instanceof InterpretNumber<? extends Number> secondNum) {
                return callback.apply(firstNum, secondNum);
            }
            self.throwError("Unsupported operation \"" + self.getName() + "\" with non-numeric values");
            return null;
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

    BiFunction<InterpretNumber<? extends Number>, InterpretNumber<? extends Number>, ? extends InterpretResult> alwaysFloat(BiFunction<Double, Double, Double> floatCallback) {
        return (first, second) -> new InterpretNumber<>(floatCallback.apply(first.getValue().doubleValue(), second.getValue().doubleValue()));
    }

    Type typeFor(GenericOperatorNode self) {
        if (self.getType() != ANY) return self.getType();
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
                self.throwError("Unsupported operation \"" + self.getName() + "\" with non-numeric values: " + first.getType() + " and " + second.getType());
                return null;
            }
        }
    }

    Operand compileNumericalBinary(AssemblyFile file, GenericOperatorNode self, Instruction intInstruction, Instruction floatInstruction) {
        return compileNumericalBinary(file, self, () -> {
            file.add(intInstruction, RBX.toOperand(), RDX.toOperand())
                    .add(MOV, RDX.toOperand(), RAX.toOperand());
        }, () -> {
            file.add(floatInstruction, RBX.toOperand(), RDX.toOperand())
                    .add(MOV, RDX.toOperand(), RAX.toOperand());
        });
    }

    /**
     * Compiles a numerical binary operation, and calls the appropriate callback based on the types of the operands.
     * The first operand will be in RDX, and the second operand will be in RBX.
     * The result should always be in RAX.
     *
     * @param file
     * @param self
     * @param ifBothIntegers
     * @param ifFloat
     */
    Operand compileNumericalBinary(AssemblyFile file, GenericOperatorNode self, Runnable ifBothIntegers, Runnable ifFloat) {
        Operand first = self.getFirst().compileValue(file);
        //TODO optimize by checking for if possible to store in registers w/o being overwritten
        //TODO check all compileValue functions to make sure that all volatile data is stored
        //TODO all integers/doubles will be stored in registers, and unused values will be popped before returning
        file.push(RDX.toOperand())
                .mov(first, RDX.toOperand());
        Operand second = self.getSecond().compileValue(file);
        file.mov(second, RBX.toOperand());

        if (self.getFirst().getType().equals(NumberType.INTEGER) && self.getSecond().getType().equals(NumberType.INTEGER)) {
            ifBothIntegers.run();
        } else {
            ifFloat.run();
        }

        file.pop(RDX.toOperand());
        return RAX.toOperand();
    }
}
