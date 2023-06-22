package systems.merl.compiler.core;

public abstract class Source {

    public abstract String getName();

    public abstract Module getModule();

    // called only once, provides text for the entire source
    protected abstract String readText();

    private SourceCode sourceCode;

    // lazily load
    public SourceCode getSourceCode() {
        if (sourceCode == null) {
            sourceCode = new SourceCode(this, readText());
        }
        return sourceCode;
    }


}
