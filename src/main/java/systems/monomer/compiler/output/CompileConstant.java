package systems.monomer.compiler.output;

import systems.monomer.compiler.output.size.CompileSize;

/**
 * Types of integer, char, string, etc
 */
//TODO
public class CompileConstant implements CompileValue{
    public int coerceInt(){
        return 0;
    }

    @Override
    public CompileValue simplify() {
        return null;
    }

    @Override
    public CompileSize getSize() {
        return null;
    }
}
