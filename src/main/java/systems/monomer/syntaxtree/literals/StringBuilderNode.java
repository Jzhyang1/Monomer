package systems.monomer.syntaxtree.literals;

import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretStringValue;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.syntaxtree.Node;

import java.util.Collection;
import java.util.stream.Collectors;

public class StringBuilderNode extends LiteralNode {

    public StringBuilderNode(Collection<? extends Node> list) {
        super("stringbuilder");
        getChildren().addAll(list);
    }

    public InterpretValue interpretValue() {
        return new InterpretStringValue(getChildren().stream().map(x -> x.interpretValue().valueString()).collect(Collectors.joining()));
    }

    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
