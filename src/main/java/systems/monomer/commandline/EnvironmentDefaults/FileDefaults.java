package systems.monomer.commandline.EnvironmentDefaults;

import lombok.experimental.UtilityClass;
import systems.monomer.Constants;
import systems.monomer.interpreter.IOType;
import systems.monomer.syntaxtree.Node;
import systems.monomer.variables.VariableKey;

import java.io.InputStream;
import java.io.OutputStream;

import static systems.monomer.interpreter.InterpretURI.URI;

@UtilityClass
public class FileDefaults {
    public void initGlobal(Node global, InputStream input, OutputStream output) {
        IOType io = new IOType(input, output);

        VariableKey ioVar = new VariableKey();
        ioVar.setValue(io.defaultValue());
        ioVar.setType(io);
        global.putVariable("io", ioVar);

        VariableKey uriVar = new VariableKey();
        uriVar.setValue(URI);
        uriVar.setType(URI);
        global.putVariable("uri", uriVar);
    }

    public void initGlobal(Node global) {
        initGlobal(global, Constants.getListener(), Constants.getOut());
    }
}
