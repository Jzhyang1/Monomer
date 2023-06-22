package systems.merl.compiler.core;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import systems.merl.compiler.util.Pair;

@EqualsAndHashCode
public final class CodeIndex implements Comparable<CodeIndex> {

    private final static int UNINITIALIZED_VALUE = -1;
    @Getter
    private final int index;
    @Getter
    private final Source source;

    private int line = UNINITIALIZED_VALUE;
    private int column = UNINITIALIZED_VALUE;

    public CodeIndex(int index, Source source) {
        source.getSourceCode().performIndexCheck(index);

        this.index = index;
        this.source = source;
    }

    public CodeIndex(int line, int column, Source source) {
        this.source = source;
        this.line = line;
        this.column = column;
        this.index = source.getSourceCode().calculateIndex(line, column);
    }

    // we can assume the values are correct
    CodeIndex(int index, Source source, int line, int column) {
        this.index = index;
        this.source = source;
        this.line = line;
        this.column = column;
    }

    public void initializeValues() {
        if (isInitialized()) {
            return;
        }

        Pair<Integer, Integer> lineAndColumn = source.getSourceCode().calculateLineAndColumn(index);
        line = lineAndColumn.getFirst();
        column = lineAndColumn.getSecond();
    }

    public boolean isInitialized() {
        return line != UNINITIALIZED_VALUE && column != UNINITIALIZED_VALUE;
    }

    public int getLine() {
        initializeValues();
        return line;
    }

    public int getColumn() {
        initializeValues();
        return column;
    }

    public CodeRange to(CodeIndex other) {
        return new CodeRange(this, other);
    }

    @Override
    public int compareTo(CodeIndex o) {
        if (source != o.source) {
            throw new IllegalArgumentException("Cannot compare CodeIndices from different sources" );
        }
        return Integer.compare(index, o.index);
    }

    public CodeIndex withOffset(int offset) {
        return new CodeIndex(index + offset, source);
    }

    public CodeRange toSingleRange() {
        return new CodeRange(this, this);
    }

    public CodeIndex lineStart() {
        return new CodeIndex(
                source.getSourceCode().getLineStart(getLine()),
                source
        );
    }

    public CodeIndex lineEnd() {
        return new CodeIndex(
                source.getSourceCode().getLineEnd(getLine()),
                source
        );
    }

    public CodeRange lineRange() {
        return new CodeRange(
                lineStart(),
                lineEnd()
        );
    }

    public String readLine() {
        return lineRange().read();
    }

    public char readChar() {
        return source.getSourceCode().charAt(index);
    }

    @Override
    public String toString() {
        return source.getName() + " (line " + getLine() + ", column " + getColumn() + ")";
    }
}