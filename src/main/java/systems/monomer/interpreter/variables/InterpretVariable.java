package systems.monomer.interpreter.variables;

import systems.monomer.interpreter.InterpretValue;
import systems.monomer.variables.VariableKey;

public interface InterpretVariable {

    /**
     * Returns a reference to the value of the variable.
     * Use clone() to get a copy of the value.
     * @return a reference to the value of the variable
     */
    //TODO implement and make non-abstract
    public abstract InterpretValue getValue();
    public abstract void setValue(InterpretValue value);

    public boolean isLocked();
    public void lock();
}