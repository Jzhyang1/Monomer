package systems.merl.monomer.tokenizer;

import java.io.File;
import java.io.FileReader;

public class SourceFile extends Source {
    public FileReader reader;
    public File file;
    public String fileName;

    public SourceFile(String path) {
        throw new Error("TODO unimplemented");
    }

    public String getTitle() {
        return fileName;
    }

    protected void bufferLines(int num) {
        throw new Error("TODO unimplemented");
    }
}
