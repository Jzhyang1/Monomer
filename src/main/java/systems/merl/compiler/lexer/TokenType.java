package systems.merl.compiler.lexer;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("StaticInitializerReferencesSubClass")
public sealed class TokenType {

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static final class SingletonTokenType extends TokenType {
        private final String name;
    }

    public static final TokenType KEYWORD = new SingletonTokenType("KEYWORD");
    public static final TokenType IDENTIFIER = new SingletonTokenType("IDENTIFIER");
    public static final TokenType LINE_COMMENT = new SingletonTokenType("LINE_COMMENT");
    public static final TokenType BLOCK_COMMENT = new SingletonTokenType("BLOCK_COMMENT");
    public static final TokenType WHITESPACE = new SingletonTokenType("WHITESPACE");
    public static final TokenType BOOLEAN_LITERAL = new SingletonTokenType("BOOLEAN_LITERAL");
    public static final TokenType CHAR_LITERAL = new SingletonTokenType("CHAR_LITERAL");
    public static final TokenType NEWLINE = new SingletonTokenType("NEWLINE");
    public static final TokenType INDENTATION = new SingletonTokenType("INDENTATION");

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static final class StringLiteral extends TokenType {
        private final boolean multiline;
    }





}
