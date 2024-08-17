package systems.monomer.compiler.output.operations;

import java.util.ArrayList;
import java.util.List;

public abstract class CompileOperation {
    private final List<CompileOperation> operands = new ArrayList<>();

    public abstract String getAssemblyString();
}
