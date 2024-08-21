package systems.monomer.tokenizer;

import lombok.NonNull;
import systems.monomer.syntaxtree.Node;

import java.util.function.Supplier;

public class OperatorEntry {
    final int leftPrec;
    final int rightPrec;
    final int info;
    private final Supplier<Node> constructor;

    public OperatorEntry(int info, int leftPrec, int rightPrec, @NonNull Supplier<Node> constructor) {
        this.info = info;
        this.leftPrec = leftPrec;
        this.rightPrec = rightPrec;
        this.constructor = constructor;
    }

    public Node getOperator() {
        return constructor.get();
    }
}
