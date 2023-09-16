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
public class TypeDefaults {
    public void initGlobal(Node global) {
        global.putVariable("bool", new VariableKey() {{
            setValue(new InterpretBool(false));
            setType(InterpretBool.BOOL);
        }});
        global.putVariable("int", new VariableKey() {{
            setValue(new InterpretNumber<>(0));
            setType(InterpretNumber.INTEGER);
        }});
        global.putVariable("float", new VariableKey() {{
            setValue(new InterpretNumber<>(0.0));
            setType(InterpretNumber.FLOAT);
        }});
        global.putVariable("char", new VariableKey() {{
            setValue(new InterpretChar('\0'));
            setType(CharType.CHAR);
        }});
        global.putVariable("string", new VariableKey() {{
            setValue(new InterpretString(""));
            setType(StringType.STRING);
        }});
    }
}
