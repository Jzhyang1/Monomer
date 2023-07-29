package systems.monomer.tokenizer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SourceFile extends Source {
    private BufferedReader reader;
    private File file;
    private String fileName;
    private int lineNumber = 0;

    public SourceFile(String path) {
        try {
            file = new File(path);
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        fileName = path;
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
                    break;  //TODO eof
                }
                buffer.add(new Line(nextLine, lineNumber + i));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        lineNumber += num;
    }
}
