package systems.merl.compiler;

import systems.merl.compiler.core.CodeIndex;
import systems.merl.compiler.core.Source;
import systems.merl.compiler.error.AnnotatedCodeBlock;

public class Run {
    public static void main(String[] args) {
        Source sc = Source.fromText("test", """
                hel
                lo
                asdf
                dfd
                df
                wor
                ld
                """);
        AnnotatedCodeBlock block = AnnotatedCodeBlock.from(sc.getSourceCode().getCompleteRange())
                .annotate(1, "hello world", '^')
                .annotate(sc.getSourceCode().length() - 2, "this should be an R lmao", '*');
        System.out.println(new CodeIndex(9, sc).readChar());
        System.out.println(block.render(true));
    }
}
