package systems.monomer.commandline.EnvironmentDefaults;

import lombok.experimental.UtilityClass;
import systems.monomer.syntaxtree.Node;
import systems.monomer.variables.VariableKey;

import static systems.monomer.interpreter.InterpretIO.STDIO;
import static systems.monomer.interpreter.InterpretURI.URI;

@UtilityClass
public class FileDefaults {
    public void initGlobal(Node global) {
        VariableKey ioVar = new VariableKey() {{
            setValue(STDIO);
            setType(STDIO);
        }};
        global.putVariable("io", ioVar);

        VariableKey uriVar = new VariableKey() {{
            setValue(URI);
            setType(URI);
        }};
        global.putVariable("uri", uriVar);
    }
}
