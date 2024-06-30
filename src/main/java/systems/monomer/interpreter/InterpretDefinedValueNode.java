package systems.monomer.interpreter;

import systems.monomer.syntaxtree.DefinedValueNode;

import java.util.function.Supplier;

public class InterpretDefinedValueNode extends DefinedValueNode {
    private final Supplier<? extends InterpretResult> interpret;

    public InterpretDefinedValueNode(Supplier<? extends InterpretResult> interpret) {
        this.interpret = interpret;
    }

    public InterpretResult interpret() {
        return interpret.get();
    }
}
