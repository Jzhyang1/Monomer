package systems.monomer;

import org.junit.Test;
import systems.monomer.compiler.Compiler;
import systems.monomer.execution.Constants;
import systems.monomer.tokenizer.SourceString;

public class CompileTest {
    @Test
    public void testCompile() {
        Compiler.compile(new SourceString("io write(\"Hello, world!\")"), false, Constants.getListener(), Constants.getOut());
    }
}
