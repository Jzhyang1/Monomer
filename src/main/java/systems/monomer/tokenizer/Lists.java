package systems.monomer.tokenizer;

import systems.monomer.interpreter.InterpretCollection;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretString;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.operators.GenericOperatorNode;
import systems.monomer.types.StringType;
import systems.monomer.types.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class Lists {
    public static Function<GenericOperatorNode, InterpretResult> listChecked(Function<List<? extends InterpretCollection>, InterpretValue> callback) {
        return (self) -> {
            List<InterpretCollection> values = new ArrayList<>();
            for (Node node : self.getChildren()) {
                InterpretResult result = node.interpretValue();
                if(!result.isValue()) throw new RuntimeException("returning from an operation");

                InterpretValue value = result.asValue();

                if (value instanceof InterpretCollection collection) {
                    values.add(collection);
                } else {
                    throw self.syntaxError("Unsupported operation \"" + self.getName() + "\" with non-list values");
                }
            }
            return callback.apply(values);
        };
    }
    public static Function<GenericOperatorNode, InterpretResult> listStringChecked(Function<List<? extends InterpretCollection>, InterpretValue> callbackCollection, Function<List<? extends InterpretString>, InterpretValue> callbackString) {
        return (self) -> {
            List<Node> children = self.getChildren();
            Type type = children.get(0).getType();
            if(type.equals(StringType.STRING)) {
                List<InterpretString> values = new ArrayList<>();
                for (Node node : children) {
                    InterpretResult result = node.interpretValue();
                    if(!result.isValue()) throw new RuntimeException("returning from an operation");

                    InterpretValue value = result.asValue();
                    if (value instanceof InterpretString string) {
                        values.add(string);
                    } else {
                        throw self.syntaxError("Unsupported operation \"" + self.getName() + "\" with non-string values");
                    }
                }
                return callbackString.apply(values);
            }
            else {
                List<InterpretCollection> values = new ArrayList<>();
                for (Node node : children) {
                    InterpretResult result = node.interpretValue();
                    if(!result.isValue()) throw new RuntimeException("returning from an operation");

                    InterpretValue value = result.asValue();
                    if (value instanceof InterpretCollection collection) {
                        values.add(collection);
                    } else {
                        throw self.syntaxError("Unsupported operation \"" + self.getName() + "\" with non-list values");
                    }
                }
                return callbackCollection.apply(values);
            }
        };
    }
}
