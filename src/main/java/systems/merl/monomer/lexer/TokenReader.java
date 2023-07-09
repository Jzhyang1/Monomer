package systems.merl.monomer.lexer;

@FunctionalInterface
public interface  TokenReader {

    LexerToken readToken(Lexer lexer);

}
