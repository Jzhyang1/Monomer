package systems.monomer.interpreter.variables;

import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.object.ObjectType;
import systems.monomer.variables.FieldKey;
import systems.monomer.variables.Key;

import static systems.monomer.errorhandling.ErrorBlock.programError;

public class InterpretFieldKey extends FieldKey implements InterpretVariable {
    public InterpretFieldKey(String name) {
        super(name);
    }
    public InterpretFieldKey(String name, Key parent) {
        super(name, parent);
    }


    public InterpretValue getValue() {
        InterpretValue parentValue = ((InterpretVariable) getParent()).getValue();
        return parentValue.getField(getName());
    }

    public void setValue(InterpretValue value) {
        if(((InterpretVariable) getParent()).getValue() instanceof ObjectType objectType)
            objectType.setField(getName(), value);
        else {
            throw programError("Can not access field " + getName() + " of " + getParent(), ErrorBlock.Reason.RUNTIME);
        }
    }
}
