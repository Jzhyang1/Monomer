package systems.monomer.compiler.output;

import systems.monomer.compiler.output.size.CompileSize;

public interface CompileValue {

    CompileValue simplify();

    CompileSize getSize();
}
