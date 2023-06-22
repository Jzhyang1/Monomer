package systems.merl.compiler.lexer;

import lombok.Data;
import systems.merl.compiler.core.CodeRange;

@Data
public class Lexer {

    private final CodeRange range;
    private final Cursor cursor = new Cursor(
            range.getSource().getSourceCode(),
            range.getStartIndex(),
            range.length()
    );

    int stringInterpolationDepth = 0;



}
