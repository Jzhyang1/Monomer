package systems.monomer.tokenizer;

import lombok.Getter;
import systems.monomer.errorHandling.ErrorBlock;
import systems.monomer.errorHandling.Index;
import systems.monomer.syntaxTree.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Token extends ErrorBlock {
    public static enum Usage {
        OPERATOR, STRING_BUILDER, STRING, CHARACTER, INTEGER, FLOAT, GROUP, IDENTIFIER,
        CHARACTER_FROM_INT
    }

    @Getter
    private String value;
    private final List<Token> children = new ArrayList<>();
    @Getter
    private final Usage usage;

    public Token(Usage usage, String value) {
        this.usage = usage;
        this.value = value;
    }

    public Token(Usage usage) {
        this.usage = usage;
    }

    public Node toNode() {
        throw new Error("TODO unimplemented");
    }

    public void add(Token child) {
        children.add(child);
    }
    public void addAll(Token token) {
        children.addAll(token.children);
    }

    public Token getLast() {
        return children.get(children.size() - 1);
    }

    public Token with(String value) {
        this.value = value;
        return this;
    }
    public Token with(Index start, Index stop, Source source) {
        setContext(start, stop, source);
        return this;
    }

    public void addSeparator() {
        add(new Token(Usage.OPERATOR, ";"));
    }

    public String toString() {
        return usage + " " + value + (children.isEmpty() ? "" : children.size() == 1 ? children :
               "[\n\t"+children.stream().map(Objects::toString)
                       .collect(Collectors.joining(",\n\t"))+"\n\t]\n");
    }
}
