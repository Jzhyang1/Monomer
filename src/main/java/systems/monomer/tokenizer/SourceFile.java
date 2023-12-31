package systems.monomer.tokenizer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SourceFile extends Source {
    private final BufferedReader reader;
    private final File file;
    private final String fileName;
    private int position = 0;
    private int lineNumber = 0;

    public SourceFile(String path) {
        this(new File(path));
    }
    public SourceFile(File sourceFile) {
        try {
            file = sourceFile;
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        fileName = sourceFile.getPath();
        init();
    }

    public List<String> getCodeBlock(int startLine, int endLine) {
        BufferedReader tempReader = null;
        try {
            tempReader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        ArrayList<String> ret = new ArrayList<>();

        try {
            for (int i = 0; i < startLine; ++i) {
                tempReader.readLine();
            }
            for (int i = startLine; i <= endLine; ++i) {
                ret.add(tempReader.readLine());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    public String getTitle() {
        return fileName;
    }

    protected void bufferLines(int num) {
        try {
            for (int i = 0; i < num; ++i) {
                String nextLine = reader.readLine();
                if(nextLine == null) {
                    num = i;
                    break;
                }
                buffer.add(new Line(nextLine, lineNumber + i, position));
                position += nextLine.length() + 1;  //+1 for newline
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        lineNumber += num;
    }

    protected int getPosition() {
        return position;
    }
}
