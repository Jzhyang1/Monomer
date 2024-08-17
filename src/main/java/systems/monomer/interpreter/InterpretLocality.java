package systems.monomer.interpreter;

import systems.monomer.interpreter.variables.InterpretKey;
import systems.monomer.variables.Locality;
import systems.monomer.variables.VariableKey;

public interface InterpretLocality extends Locality {
    default void initVariables() {
        //TODO call key.setType(key.getType().simplify()) for all keys during the simplify stage
        for(VariableKey key : getVariables().values()) {
            ((InterpretKey) key).setValue(key.getType().defaultValue());
        }
    }
}
