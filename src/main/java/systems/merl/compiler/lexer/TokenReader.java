package systems.merl.compiler.lexer;

@FunctionalInterface
public interface  TokenReader {

    LexerToken readToken(Lexer lexer);

}
