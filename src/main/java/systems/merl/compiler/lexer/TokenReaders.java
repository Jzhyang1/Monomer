package systems.merl.compiler.lexer;

import lombok.experimental.UtilityClass;
import systems.merl.compiler.core.CodeRange;

import java.util.function.BiFunction;

@UtilityClass
public class TokenReaders {

    public BiFunction<String, CodeRange, TokenType> always(TokenType type) {
        return (s, r) -> type;
    }

    public TokenReader LINE_COMMENT = new RegexReader("#[^\\n]*", always(TokenType.LINE_COMMENT));
    public TokenReader NEWLINE = new RegexReader("\\n", always(TokenType.LINE_COMMENT));

}
