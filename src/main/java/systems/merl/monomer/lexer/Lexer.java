package systems.merl.monomer.lexer;

import lombok.Data;

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
