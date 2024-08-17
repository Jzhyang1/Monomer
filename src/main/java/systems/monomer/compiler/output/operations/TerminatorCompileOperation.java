package systems.monomer.compiler.output.operations;

//TODO
public class TerminatorCompileOperation extends CompileOperation{
    public static TerminatorCompileOperation TERMINATE = new TerminatorCompileOperation();

    private TerminatorCompileOperation(){}
    @Override
    public String getAssemblyString() {
        return "";
    }
}
