package systems.monomer.commandline.EnvironmentDefaults;

import lombok.experimental.UtilityClass;
import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.*;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.literals.LiteralNode;
import systems.monomer.types.OverloadedFunctionType;
import systems.monomer.types.Type;
import systems.monomer.variables.VariableKey;

import java.io.File;
import java.util.List;
import java.util.function.Function;

import static systems.monomer.interpreter.IOType.STDIO;
import static systems.monomer.interpreter.InterpretURI.URI;
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
            return (new IOType(uri)).defaultValue();   //TODO looks weird
        });
    }

    private void putConvert(OverloadedFunctionType overload, Type from, Type to,
                            Function<InterpretValue, InterpretResult> convertFunc) {
        overload.putSystemOverload(List.of(from), (args) -> new LiteralNode() {
            @Override
            public Type getType() {
                return to;
            }

            @Override
            public InterpretResult interpretValue() {
                return convertFunc.apply(args.get(0).interpretValue());
            }

            public Operand compileValue(AssemblyFile file) {
                throw new Error("TODO unimplemented");
            }

            @Override
            public CompileSize compileSize() {
                syntaxError("TODO unimplemented");
                return null;
            }
        });
    }
}
