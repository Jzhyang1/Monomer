package systems.monomer.compiler.output.operations;

import systems.monomer.compiler.output.CompileValue;
import systems.monomer.compiler.output.size.CompileSize;

//TODO
public abstract class InlineCompileOperation extends CompileOperation implements CompileValue {
    @Override
    public CompileValue simplify() {
        return null;
    }

    @Override
    public CompileSize getSize() {
        return null;
    }

    @Override
    public String getAssemblyString() {
        return "";
    }
}
