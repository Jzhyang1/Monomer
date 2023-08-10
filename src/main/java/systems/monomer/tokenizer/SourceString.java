package systems.monomer.tokenizer;

import java.util.Arrays;
import java.util.List;

public class SourceString extends Source {
    private String[] value;
    private int lineNumber = 0;
    private int position = 0;

    public SourceString(String value) {
        this.value = value.split("\n");
        init();
    }

    public List<String> getCodeBlock(int startLine, int endLine) {
        return Arrays.asList(Arrays.copyOfRange(value, startLine, endLine + 1));
    }

    public String getTitle() {
        return "String source";
    }
    protected void bufferLines(int num) {
        int i = 0;
        while(i < num && lineNumber+i < value.length) {
            buffer.add(new Line(value[lineNumber+i], lineNumber+i, position));
            position += value[lineNumber+i].length() + 1;   //1 for newline
            ++i;
        }
        lineNumber += i;
    }

    protected int getPosition() {
        return position;
    }
}
