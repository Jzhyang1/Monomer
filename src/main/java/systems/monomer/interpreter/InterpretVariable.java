package systems.monomer.interpreter;

import systems.monomer.types.ObjectType;
import systems.monomer.variables.VariableKey;

public abstract class InterpretVariable extends ObjectType implements InterpretValue {
    //TODO
    public abstract void setValue(InterpretValue value);

    public VariableKey clone() {
        throw new Error("Can not clone VariableKey");
    }
}