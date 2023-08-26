package systems.monomer.syntaxtree.operators;

import systems.monomer.interpreter.InterpretCollection;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class Lists {
    public static Function<GenericOperatorNode, InterpretValue> listChecked(Function<List<? extends InterpretCollection>, InterpretValue> callback) {
        return (self) -> {
            List<InterpretCollection> values = new ArrayList<>();
            for (Node node : self.getChildren()) {
                InterpretValue value = node.interpretValue();
                if (value instanceof InterpretCollection collection) {
                    values.add(collection);
                } else {
                    self.throwError("Unsupported operation \"" + self.getName() + "\" with non-list values");
                    return null;
                }
            }
            return callback.apply(values);
        };
    }
}
