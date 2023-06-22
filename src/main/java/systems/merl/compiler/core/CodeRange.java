package systems.merl.compiler.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import systems.merl.compiler.util.Pair;

import java.util.Iterator;

// includes the end index
@EqualsAndHashCode
public final class CodeRange implements Iterable<CodeIndex> {

    private static final int UNINITIALIZED_VALUE = -1;

    @Getter
    private final Source source;
    @Getter
    private final int startIndex;
    @Getter
    private final int endIndex;

    private int startLine = UNINITIALIZED_VALUE;
    private int startColumn = UNINITIALIZED_VALUE;
    private int endLine = UNINITIALIZED_VALUE;
    private int endColumn = UNINITIALIZED_VALUE;

    public CodeRange(Source source, int startIndex, int endIndex) {
        source.getSourceCode().performIndexCheck(startIndex);
        source.getSourceCode().performIndexCheck(endIndex);

        if (startIndex > endIndex) {
            throw new IllegalArgumentException("Start index must be less than or equal to end index");
        }

        this.source = source;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public CodeRange(CodeIndex start, CodeIndex end) {
        this(start.getSource(), start.getIndex(), end.getIndex());
        if (!start.getSource().equals(end.getSource())) {
            throw new IllegalArgumentException("Start and end indices must be from the same source");
        }
        this.startLine = start.getLine();
        this.startColumn = start.getColumn();
        this.endLine = end.getLine();
        this.endColumn = end.getColumn();
    }

    public void initializeValues() {
        if (isInitialized()) {
            return;
        }

        Pair<Integer, Integer> startLineAndColumn = source.getSourceCode().calculateLineAndColumn(startIndex);
        startLine = startLineAndColumn.getFirst();
        startColumn = startLineAndColumn.getSecond();

        Pair<Integer, Integer> endLineAndColumn = source.getSourceCode().calculateLineAndColumn(endIndex);
        endLine = endLineAndColumn.getFirst();
        endColumn = endLineAndColumn.getSecond();
    }

    public boolean isInitialized() {
        return startLine != UNINITIALIZED_VALUE &&
                startColumn != UNINITIALIZED_VALUE &&
                endLine != UNINITIALIZED_VALUE &&
                endColumn != UNINITIALIZED_VALUE;
    }

    public int getStartLine() {
        initializeValues();
        return startLine;
    }

    public int getStartColumn() {
        initializeValues();
        return startColumn;
    }

    public int getEndLine() {
        initializeValues();
        return endLine;
    }

    public int getEndColumn() {
        initializeValues();
        return endColumn;
    }

    public CodeIndex getStart() {
        return new CodeIndex(startIndex, source, startLine, startColumn);
    }

    public CodeIndex getEnd() {
        return new CodeIndex(endIndex, source, endLine, endColumn);
    }

    public int length() {
        return endIndex - startIndex + 1;
    }

    public boolean contains(CodeIndex index) {
        return index.getIndex() >= startIndex && index.getIndex() <= endIndex;
    }

    public boolean isSingleLine() {
        return startLine == endLine;
    }

    public int getLineCount() {
        return endLine - startLine + 1;
    }

    public CodeRange completeLines() {
        CodeIndex start = getStart().lineStart();
        CodeIndex end = getEnd().lineEnd();
        return start.to(end);
    }

    public boolean isSingleIndex() {
        return startIndex == endIndex;
    }

    public CodeIndex asSingleIndex() {
        if (!isSingleIndex()) {
            throw new IllegalStateException("Code range is not a single index");
        }
        return getStart();
    }

    public String read() {
        return source.getSourceCode().slice(startIndex, endIndex);
    }

    @Override
    public Iterator<CodeIndex> iterator() {
        return new Iterator<>() {
            private int currentIndex = startIndex;

            @Override
            public boolean hasNext() {
                return currentIndex <= endIndex;
            }

            @Override
            public CodeIndex next() {
                return new CodeIndex(currentIndex++, source);
            }
        };
    }

    @Override
    public String toString() {
        if (startIndex == endIndex) {
            return getStart().toString();
        }
        if (startLine == endLine) {
            return source.getName() + " (line " + startLine + ", column " + startColumn + " - " + endColumn + ")";
        }
        return source.getName() + " (line " + startLine + ", column " + startColumn + ") - (line " + endLine + ", column " + endColumn + ")";
    }
}
