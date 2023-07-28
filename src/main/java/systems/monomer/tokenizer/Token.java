package systems.monomer.tokenizer;

import lombok.Getter;
import systems.monomer.errorHandling.ErrorBlock;
import systems.monomer.syntaxTree.Node;

import java.util.ArrayList;
import java.util.List;

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

    public Token with(String value) {
        this.value = value;
        return Token.this;
    }
    public void addSeparator() {
        add(new Token(Usage.OPERATOR, ";"));
    }

    public String toString() {
        return usage + " " + value + (children.isEmpty() ? "" : children);
    }
}
