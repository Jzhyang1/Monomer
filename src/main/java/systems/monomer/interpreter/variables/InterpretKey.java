package systems.monomer.interpreter.variables;

import lombok.Getter;
import lombok.Setter;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.variables.VariableKey;

@Getter
public class InterpretKey extends VariableKey implements InterpretVariable {
    @Setter
    private InterpretValue value;
}
