package systems.merl.compiler.lexer;

import lombok.Data;
import systems.merl.compiler.core.CodeRange;

@Data
public class LexerToken {

    private final TokenType type;
    private final CodeRange range;
}
