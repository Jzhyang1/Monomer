package systems.merl.monomer.lexer;

import lombok.Data;

@Data
public class LexerToken {

    private final TokenType type;
    private final CodeRange range;
}
