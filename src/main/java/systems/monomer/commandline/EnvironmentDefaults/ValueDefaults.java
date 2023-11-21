package systems.monomer.commandline.EnvironmentDefaults;

import lombok.experimental.UtilityClass;
import systems.monomer.interpreter.InterpretBool;
import systems.monomer.syntaxtree.Node;
import systems.monomer.variables.VariableKey;

@UtilityClass
public class ValueDefaults {
    public void initGlobal(Node global) {
        global.putVariable("true", new VariableKey() {{
            setType(new InterpretBool(true));
            setValue(new InterpretBool(true));
        }});
        global.putVariable("false", new VariableKey() {{
            setType(new InterpretBool(false));
            setValue(new InterpretBool(false));
        }});
    }
}
