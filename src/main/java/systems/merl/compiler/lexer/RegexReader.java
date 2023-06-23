package systems.merl.compiler.lexer;

import lombok.RequiredArgsConstructor;
import org.intellij.lang.annotations.Language;
import systems.merl.compiler.core.CodeRange;

import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class RegexReader implements TokenReader {

    private final Pattern pattern;
    private final BiFunction<String, CodeRange, TokenType> tokenTypeFunction;

    public RegexReader(
            @Language("RegExp") String pattern,
            BiFunction<String, CodeRange, TokenType> tokenTypeFunction) {
        this(Pattern.compile(pattern), tokenTypeFunction);
    }

    @Override
    public LexerToken readToken(Lexer lexer) {
        Matcher matcher = pattern.matcher(lexer.getSourceCode())
                .region(lexer.getCursor().getCurrentIndex(), lexer.getCursor().getStartingIndex() + lexer.getCursor().getLengthToRead())
                .useTransparentBounds(true)
                .useAnchoringBounds(false); // search outside
        if (!matcher.lookingAt()) {
            return null;
        }

        int startIndex = lexer.getCursor().getCurrentIndex();
        int endIndex = matcher.end();
        lexer.getCursor().advanceNextCharacters(endIndex - startIndex);

        CodeRange range = new CodeRange(lexer.getSourceCode().getSource(), startIndex, endIndex);
        TokenType type = tokenTypeFunction.apply(range.read(), range);
        return new LexerToken(type, range);
    }
}
