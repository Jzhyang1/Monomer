package systems.monomer.interpreter;

public interface InterpretResult {
    default boolean isValue() {
        return true;
    }

    //TODO asValue needs to be checked to make sure it is only used after isValue check
    // otherwise it will cause problems with break/returns
    InterpretValue asValue();
}
