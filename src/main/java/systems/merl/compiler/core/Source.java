package systems.merl.compiler.core;

import java.io.File;

public abstract class Source {

    public abstract String getName();

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

    public static Source fromText(String name, String text) {
        return new Source() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            protected String readText() {
                return text;
            }
        };
    }

    public static Source fromFile(File file) {
        return new Source() {
            @Override
            public String getName() {
                return file.getName();
            }

            @Override
            protected String readText() {
                return null;
            }
        };
    }

}
