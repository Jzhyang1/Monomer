package systems.monomer.interpreter;

public interface InterpretResult {
    default boolean isValue() {
        return true;
    }

    InterpretValue asValue();
}
