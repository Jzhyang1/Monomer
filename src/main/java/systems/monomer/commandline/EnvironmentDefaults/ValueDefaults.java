package systems.monomer.commandline.EnvironmentDefaults;

import lombok.experimental.UtilityClass;
import systems.monomer.interpreter.InterpretBool;
import systems.monomer.interpreter.InterpretChar;
import systems.monomer.interpreter.InterpretNumber;
import systems.monomer.interpreter.InterpretString;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.CharType;
import systems.monomer.types.StringType;
import systems.monomer.variables.VariableKey;

@UtilityClass
public class ValueDefaults {
    public void initGlobal(Node global) {
        global.putVariable("true", new VariableKey() {{
            setValue(new InterpretBool(true));
            setType(InterpretBool.BOOL);
        }});
        global.putVariable("false", new VariableKey() {{
            setValue(new InterpretBool(false));
            setType(InterpretBool.BOOL);
        }});
    }
}
