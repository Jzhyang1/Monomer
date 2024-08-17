package systems.monomer.variables;

import lombok.Getter;
import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.types.Type;
import systems.monomer.types.pseudo.PlaceholderType;

import static systems.monomer.errorhandling.ErrorBlock.programError;


@Getter
public class VariableKey extends Key {
    private PlaceholderType type = new PlaceholderType();

    private boolean isLocked = false;

    public VariableKey(){}

    public void setField(String field, Type type) {
        this.type.setField(field, type);
    }

    public boolean isLocked() {
        return isLocked;
    }
    public void lock() {
        isLocked = true;
    }

    public void setType(Type type) {
        if(!(type instanceof PlaceholderType pt)) {
            throw programError("Cannot set type to placeholder type", ErrorBlock.Reason.SYNTAX);
        }
        this.type = pt;
    }

    @Override
    public VariableKey clone() {
        VariableKey key = null;  //TODO also clone value, overloads, etc
        try {
            key = (VariableKey) super.clone();
        } catch (CloneNotSupportedException e) {
            throw programError("clone failed", ErrorBlock.Reason.OTHER);
        }
        return key;
    }
}
