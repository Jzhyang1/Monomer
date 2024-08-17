package systems.monomer.execution.environmentDefaults;

import lombok.experimental.UtilityClass;
import systems.monomer.execution.Constants;
import systems.monomer.interpreter.values.InterpretIO;
import systems.monomer.interpreter.variables.InterpretKey;
import systems.monomer.types.system.IOType;
import systems.monomer.syntaxtree.Node;

import java.io.InputStream;
import java.io.OutputStream;

import static systems.monomer.interpreter.values.InterpretURI.URI;

@UtilityClass
public class FileDefaults {
    public void initGlobal(Node global, InputStream input, OutputStream output) {
        IOType io = new IOType();
        InterpretIO ioValue = new InterpretIO(input, output);

        InterpretKey ioVar = new InterpretKey();
        ioVar.setValue(ioValue);
        ioVar.setType(io);
        global.putVariable("io", ioVar);

        InterpretKey uriVar = new InterpretKey();
        uriVar.setValue(URI);
        uriVar.setType(URI);
        global.putVariable("uri", uriVar);
    }
}
