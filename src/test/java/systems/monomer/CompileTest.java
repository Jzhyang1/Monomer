package systems.monomer;

import org.junit.Test;
import systems.monomer.execution.commandline.Compile;

public class CompileTest {
    @Test
    public void testCompile() {
        Compile.compile("io write(\"Hello, world!\")");
    }
}
