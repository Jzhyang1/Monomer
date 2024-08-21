package systems.monomer.execution.environmentDefaults;

import lombok.experimental.UtilityClass;
import systems.monomer.execution.Initializer;
import systems.monomer.interpreter.*;
import systems.monomer.interpreter.values.InterpretIO;
import systems.monomer.interpreter.values.InterpretURI;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.Type;
import systems.monomer.types.signature.Signature;
import systems.monomer.variables.Overloadable;
import systems.monomer.variables.VariableKey;

import java.io.File;
import java.util.function.Function;

import static systems.monomer.interpreter.values.InterpretIO.STDIO;
import static systems.monomer.interpreter.values.InterpretURI.URI;
import static systems.monomer.types.collection.StringType.STRING;
import static systems.monomer.types.object.ObjectType.EMPTY;

@UtilityClass
public class ConvertDefaults {
    public final String NAME = "convert";

    public void initGlobal(Node global) {
        VariableKey key = Initializer.init.variableKey();
        global.putVariable(NAME, key);

        Overloadable overload = new Overloadable();
        key.setType(overload);

        putConvert(overload, STRING, URI, (value) -> new InterpretURI(value.getValue()));
        putConvert(overload, URI, STDIO, (value) -> {
            File uri = ((InterpretURI) value).getUri();
            assert uri != null;
            return new InterpretIO(uri);
        });
    }

    private void putConvert(Overloadable overload, Type from, Type to,
                            Function<InterpretValue, InterpretResult> convertFunc) {
        overload.add(new Signature(from, EMPTY, to));
        //TODO right before execution, simplify overload and search and set all convert signatures to convertFunc
        overload.putSingleInterpretOverload(from, to, convertFunc);
    }
}
