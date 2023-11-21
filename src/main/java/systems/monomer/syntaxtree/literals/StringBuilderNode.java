package systems.monomer.syntaxtree.literals;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretString;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.StringType;

import java.util.Collection;
import java.util.stream.Collectors;

public class StringBuilderNode extends LiteralNode {

    public StringBuilderNode(Collection<? extends Node> list) {
        super("stringbuilder");
        getChildren().addAll(list);
    }

    @Override
    public void matchTypes() {
        super.matchTypes();
        setType(StringType.STRING);
    }

    public InterpretValue interpretValue() {
        return new InterpretString(
                getChildren().stream()
                        .map(x -> x.interpretValue().asValue().valueString())
                        .collect(Collectors.joining()));
    }

    public Operand compileValue(AssemblyFile file) {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
