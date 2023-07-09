import java.util.Collection;

public class SourceLine {
    private int row;
    private int col;
    private String line;
    private Source source;

    public SourceLine(String line, int row, Source source) {
        this.line = line;
        this.row = row;
        this.source = source;
        col = 1;
    }

    public String getFullLine() {
        return line;
    }

    public char peek() {
        if (col <= line.length()) {
            return line.charAt(col-1);
        } else {
            return '\n';
        }
    }

    public char get() {
        char c = peek();
        col++;
        return c;
    }

    public String matchNext(Collection<String> tokens) {
        for (String token : tokens) {
            if (line.startsWith(token, col-1)) {
                col += token.length();
                return token;
            }
        }
        return null;
    }

    public int startingSpaces() {
        int count = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ' ') {
                count++;
            } 
            else if(line.charAt(i) == '\t') {
                count += 4;
            }
            else {
                break;
            }
        }
        return count;
    }

    public void skipSpaces() {
        while (peek() == ' ') {
            get();
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public LineIndex getIndex() {
        return new LineIndex(row, col);
    }
}
