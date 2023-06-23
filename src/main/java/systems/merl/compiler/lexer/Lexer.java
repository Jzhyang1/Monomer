package systems.merl.compiler.lexer;

import lombok.Data;
import systems.merl.compiler.core.CodeRange;
import systems.merl.compiler.core.SourceCode;

@Data
public class Lexer {

    private final CodeRange range;
    private final Cursor cursor;

    public Lexer(CodeRange range) {
        this.range = range;
        this.cursor = new Cursor(
                range.getSource().getSourceCode(),
                range.getStartIndex(),
                range.length()
        );
    }

    int stringInterpolationDepth = 0;

    public SourceCode getSourceCode() {
        return range.getSource().getSourceCode();
    }

}
