package systems.monomer.interpreter.execution;

import org.jetbrains.annotations.NotNull;
import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.values.*;
import systems.monomer.syntaxtree.operators.GenericOperatorNode;
import systems.monomer.types.Type;
import systems.monomer.types.collection.CollectionType;
import systems.monomer.types.primitive.BoolType;
import systems.monomer.types.primitive.CharType;
import systems.monomer.types.primitive.FloatType;
import systems.monomer.types.primitive.IntType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static systems.monomer.errorhandling.ErrorBlock.programError;

public final class InterpretUtil {
    public static Function<GenericOperatorNode, Function<Iterator<InterpretValue>, ? extends InterpretResult>> unBi(
            Function<GenericOperatorNode, Function<Iterator<InterpretValue>, ? extends InterpretResult>> unaryCallback,
            Function<GenericOperatorNode, Function<Iterator<InterpretValue>, ? extends InterpretResult>> binaryCallback) {
        return (self) -> {
            return self.size() == 1 ? unaryCallback.apply(self) : binaryCallback.apply(self);
        };
    }

    public static Function<GenericOperatorNode, Function<Iterator<InterpretValue>, ? extends InterpretResult>> cmpOp(
            BiFunction<Integer, Integer, Boolean> intCallback,
            BiFunction<Double, Double, Boolean> floatCallback,
            BiFunction<Boolean, Boolean, Boolean> boolCallback,
            BiFunction<List<InterpretValue>, List<InterpretValue>, Boolean> colCallback
    ) {
        return (self) -> {
            if(self.getChildren().stream().allMatch(n -> n.getType() instanceof CollectionType))
                return cmpIterHandle(colCallback);

            if (self.getChildren().stream().allMatch(n -> BoolType.BOOL.typeContains(n.getType()))) {
                return cmpIterHandle(boolCallback);
            } else if (self.getChildren().stream().allMatch(n -> IntType.INT.typeContains(n.getType()))) {
                return cmpIterHandle(intCallback);
            } else {
                return cmpIterHandle(floatCallback);
            }
        };
    }

    public static Function<GenericOperatorNode, Function<Iterator<InterpretValue>, ? extends InterpretResult>> intFloatOp(
            BiFunction<Integer, Integer, Integer> intCallback,
            BiFunction<Double, Double, Double> floatCallback) {
        return (self) -> {
            if (self.getChildren().stream().allMatch(n -> IntType.INT.typeContains(n.getType()))) {
                return intIterHandle(intCallback);
            } else {
                return floatIterHandle(floatCallback);
            }
        };
    }

    public static Function<GenericOperatorNode, Function<Iterator<InterpretValue>, ? extends InterpretResult>> intFloatUniOp(
            Function<Integer, Integer> intCallback,
            Function<Double, Double> floatCallback) {
        return (self) -> {
            if (IntType.INT.typeContains(self.getFirst().getType())) {
                return (iter) -> new InterpretInt(intCallback.apply(iter.next().getValue()));
            } else {
                return (iter) -> new InterpretFloat(floatCallback.apply(iter.next().getValue()));
            }
        };
    }

    public static Function<GenericOperatorNode, Function<Iterator<InterpretValue>, ? extends InterpretResult>> intBoolOp(BiFunction<Integer, Integer, Integer> intCallback, BiFunction<Boolean, Boolean, Boolean> boolCallback) {
        return (self) -> {
            if (self.getChildren().stream().allMatch(n -> IntType.INT.typeContains(n.getType()))) {
                return intIterHandle(intCallback);
            } else if (self.getChildren().stream().allMatch(n -> BoolType.BOOL.typeContains(n.getType()))) {
                return boolIterHandle(boolCallback);
            }
            throw self.runtimeError("Unsupported operation \"" + self.getName() + "\" between " + self.getFirst().getType() + " and " + self.getSecond().getType());
        };
    }


    public static Function<GenericOperatorNode, Function<Iterator<InterpretValue>, ? extends InterpretResult>> floatOp(BiFunction<Double, Double, Double> floatCallback) {
        return (self) -> floatIterHandle(floatCallback);
    }

    public static Function<GenericOperatorNode, Function<Iterator<InterpretValue>, ? extends InterpretResult>> boolOp(Function<Boolean, Boolean> boolCallback) {
        return (self) -> {
            assert self.size() == 1;
            return (iter) -> new InterpretBool(boolCallback.apply(iter.next().getValue()));
        };
    }

    public static Function<GenericOperatorNode, Function<Iterator<InterpretValue>, ? extends InterpretResult>> boolOp(BiFunction<Boolean, Boolean, Boolean> boolCallback) {
        return (self) -> boolIterHandle(boolCallback);
    }


    public static Function<GenericOperatorNode, Function<Iterator<InterpretValue>, ? extends InterpretResult>> colOp(BiFunction<Collection<InterpretValue>, Collection<? extends InterpretValue>, Collection<InterpretValue>> colCallback) {
        return (self) -> {
            assert self.getChildren().stream().allMatch(c -> c.getType() instanceof CollectionType);
            return (iter) -> {
                InterpretCollection firstCol = (InterpretCollection) iter.next();
                InterpretCollection ret = firstCol.emptyCopy();
                //copy the first value for "immutability"
                //TODO optimize for mutators
                Collection<InterpretValue> prev = new ArrayList<>(
                        firstCol.getValues()
                                .stream()
                                .map(InterpretValue::clone)
                                .toList()
                );

                while(iter.hasNext()) {
                    InterpretCollection nextCol = (InterpretCollection) iter.next();
                    Collection<? extends InterpretValue> next = nextCol.getValues();

                    prev = colCallback.apply(prev, next);
                }
                ret.addAll(prev);
                return ret;
            };
        };
    }

    public static Function<GenericOperatorNode, Function<Iterator<InterpretValue>, ? extends InterpretResult>> breakingOp() {
        return (self) -> (iter) -> new InterpretBreaking(self.getName(),
                        self.size() == 0 ?
                        InterpretTuple.EMPTY :
                        iter.next());
    }

    public static Function<GenericOperatorNode, Function<Iterator<InterpretValue>, ? extends InterpretResult>> truthyOp() {
        return (self) -> {
            assert self.size() == 1;
            Type firstType = self.getFirst().getType();

            if(firstType instanceof CollectionType) return (iter) -> new InterpretBool(!((InterpretCollection) iter.next()).getValues().isEmpty());

            if (BoolType.BOOL.typeContains(firstType)) {
                return (iter) -> iter.next();
            } else if(CharType.CHAR.typeContains(firstType)) {
                //character is not a number in Java
                return (iter) -> new InterpretBool(iter.next().<Character>getValue() != '\0');
            } else if (FloatType.FLOAT.typeContains(firstType)) {
                return (iter) -> new InterpretBool(iter.next().<Number>getValue().floatValue() != 0);
            } else {
                throw self.runtimeError("Expected a bool-y value, got " + firstType);
            }
        };
    }

    /*TODO make spread a pseudo operator that instead appends all values to the collection
    *  e.g. [1, ...[2, 3]] -> [1, 2, 3] performs [1].appendAll([2, 3])
    *  in the case of
    *   [for i in [1,2,3]: if i == 1: 1 else: ...[i, i+1]] -> [1, 2, 3, 2, 3, 3, 4]
    *  perform appends for each value in the for loop and appendAll for the spread operator.
    *  Implement this in list literals and list comprehension
    */
    public static Function<GenericOperatorNode, Function<Iterator<InterpretValue>, ? extends InterpretResult>> spreadOp() {
        return (self) -> (iter) -> new InterpretSequence(((InterpretCollection)iter.next()).getValues());
    }

    public static Function<GenericOperatorNode, Function<Iterator<InterpretValue>, ? extends InterpretResult>> rangeOp() {
        return (self) -> {
            if(self.size() == 2) {
                return (iter) ->  new InterpretRanges(
                        new InterpretRange(self.getType(), iter.next(), iter.next(), new InterpretInt(1), true, true)
                );
            } else if(self.size() == 3) {
                return (iter) ->  new InterpretRanges(
                        new InterpretRange(self.getType(), iter.next(), iter.next(), iter.next(), true, true)
                );
            } else {
                throw programError("Expected 2 or 3 arguments, got " + self.size(), ErrorBlock.Reason.SYNTAX);
            }
        };
    }

    @NotNull
    public static Function<GenericOperatorNode, Function<Iterator<InterpretValue>,? extends InterpretResult>> inOp() {
        return (self) -> (iter) -> {
            InterpretValue first = iter.next();
            InterpretValue second = iter.next();
            return new InterpretBool(((InterpretCollection) second).getValues().contains(first));
        };
    }

    @NotNull
    public static Function<GenericOperatorNode, Function<Iterator<InterpretValue>,? extends InterpretResult>> sizeOp() {
        return (self) -> (iter) -> {
            InterpretValue first = iter.next();
            assert first instanceof InterpretCollection;
            return new InterpretInt(((InterpretCollection) first).size());
        };
    }

    @NotNull
    private static Function<Iterator<InterpretValue>, ? extends InterpretResult> floatIterHandle(BiFunction<Double, Double, Double> floatCallback) {
        return (iter) -> {
            double current = iter.next().<Number>getValue().doubleValue();
            while(iter.hasNext()){
                double next = iter.next().<Number>getValue().doubleValue();
                current = floatCallback.apply(current, next);
            }
            return new InterpretFloat(current);
        };
    }

    @NotNull
    private static Function<Iterator<InterpretValue>, ? extends InterpretResult> boolIterHandle(BiFunction<Boolean, Boolean, Boolean> boolCallback) {
        return (iter) -> {
            Boolean current = iter.next().getValue();
            while(iter.hasNext()){
                Boolean next = iter.next().getValue();
                current = boolCallback.apply(current, next);
            }
            return new InterpretBool(current);
        };
    }

    @NotNull
    private static Function<Iterator<InterpretValue>, ? extends InterpretResult> intIterHandle(BiFunction<Integer, Integer, Integer> intCallback) {
        return (iter) -> {
            Integer current = iter.next().getValue();
            while(iter.hasNext()){
                Integer next = iter.next().getValue();
                current = intCallback.apply(current, next);
            }
            return new InterpretInt(current);
        };
    }


    @NotNull
    private static<T> Function<Iterator<InterpretValue>, ? extends InterpretResult> cmpIterHandle(BiFunction<T, T, Boolean> callback) {
        return (iter) -> {
            T current = iter.next().getValue();
            while(iter.hasNext()){
                T next = iter.next().getValue();

                Boolean res = callback.apply(current, next);
                if(!res) return InterpretBool.FALSE;

                current = next;
            }
            return InterpretBool.TRUE;
        };
    }
}
