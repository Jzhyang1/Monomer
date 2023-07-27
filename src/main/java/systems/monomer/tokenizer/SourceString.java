package systems.monomer.tokenizer;

import java.util.Arrays;
import java.util.List;

public class SourceString extends Source {
    private String[] value;
    private int lineNumber = 0;

    public SourceString(String value) {
        this.value = value.split("\n");
    }

    public List<String> getCodeBlock(int startLine, int endLine) {
        return Arrays.asList(Arrays.copyOfRange(value, startLine, endLine + 1));
    }

    public String getTitle() {
        return "String source";
    }
    protected void bufferLines(int num) {
        for(int i = 0; i < num; ++i) {
            buffer.add(new Line(value[lineNumber+i], lineNumber+i));
        }
        lineNumber += num;
    }
}
