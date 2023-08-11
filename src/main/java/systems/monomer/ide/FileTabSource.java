package systems.monomer.ide;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.mozilla.universalchardet.ReaderFactory;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

@RequiredArgsConstructor
@Getter
public class FileTabSource implements TabSource {
    private final File file;
    private final boolean editable;
    private String contents;

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public void setName(String name) {
        if (!isEditable()) return;
        file.renameTo(new File(file.getParent(), name));
    }

    @Override
    public String getToolTipText() {
        return file.getAbsolutePath();
    }

    @Override
    @SneakyThrows
    public String getContents() {
        if (contents != null) return contents;
        BufferedReader reader = ReaderFactory.createBufferedReader(file);
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
            builder.append("\n");
        }
        reader.close();
        return (contents = builder.toString());
    }

    @Override
    @SneakyThrows
    public void setContents(String contents) {
        if (!isEditable()) return;
        this.contents = contents;
        try (PrintStream stream = new PrintStream(file)) {
            stream.print(contents);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String calculatedEncoding = null;

    @SneakyThrows
    @Override
    public String desc() {
        if (calculatedEncoding == null) {
            // file <encoding>
            calculatedEncoding = UniversalDetector.detectCharset(file);
            if (calculatedEncoding == null) {
                calculatedEncoding = "utf-8";
            }
        }
        return "file " + calculatedEncoding;
    }
}
