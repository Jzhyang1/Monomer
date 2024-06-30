package systems.monomer.commandline.EnvironmentDefaults;

import lombok.experimental.UtilityClass;
import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.*;
import systems.monomer.interpreter.values.InterpretIO;
import systems.monomer.interpreter.values.InterpretURI;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.literals.LiteralNode;
import systems.monomer.types.OverloadedFunctionType;
import systems.monomer.types.Type;
import systems.monomer.variables.VariableKey;

import java.io.File;
import java.util.List;
import java.util.function.Function;

import static systems.monomer.interpreter.values.InterpretIO.STDIO;
import static systems.monomer.interpreter.values.InterpretURI.URI;
import static systems.monomer.syntaxtree.Configuration.create;
import static systems.monomer.types.StringType.STRING;

@UtilityClass
public class ConvertDefaults {
    public final String NAME = "convert";

    public void initGlobal(Node global) {
        VariableKey key = new VariableKey();
        global.putVariable(NAME, key);

        OverloadedFunctionType overload = new OverloadedFunctionType();
        key.setType(overload);

        putConvert(overload, STRING, URI, (value) -> new InterpretURI(value.getValue()));
        putConvert(overload, URI, STDIO, (value) -> {
            File uri = ((InterpretURI) value).getUri();
            assert uri != null;
            return new InterpretIO(uri);
        });
    }

    private void putConvert(OverloadedFunctionType overload, Type from, Type to,
                            Function<InterpretValue, InterpretResult> convertFunc) {
        overload.putSingleInterpretOverload(from, to, convertFunc);
    }
}
