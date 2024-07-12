package systems.monomer.execution.environmentDefaults;

import lombok.experimental.UtilityClass;
import systems.monomer.interpreter.values.InterpretBool;
import systems.monomer.interpreter.values.InterpretChar;
import systems.monomer.interpreter.values.InterpretNumber;
import systems.monomer.interpreter.values.InterpretString;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.primative.CharType;
import systems.monomer.types.plural.StringType;
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
