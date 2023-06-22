package systems.merl.compiler.core;

import lombok.AccessLevel;
import lombok.Getter;
import systems.merl.compiler.util.Pair;

import java.util.ArrayList;
import java.util.List;

// columns and lines are 1-indexed
// so 1, 1 is the first character in the file
// indices are 0-based, so 0 is the first character in the file
// index(0).getLine() == 1
@Getter
public final class SourceCode implements CharSequence {

    private final Source source;
    private final String text;

    @Getter(AccessLevel.PACKAGE)
    private final int[] newLineIndices;

    public SourceCode(Source source, String text) {
        this.source = source;
        this.text = text;

        List<Integer> list = new ArrayList<>();
        list.add(-1);
        for (int index = 0; index < text.length(); index++) {
            if (text.charAt(index) == '\n') {
                list.add(index);
            }
        }
        list.add(text.length());
        newLineIndices = list.stream().mapToInt(i -> i).toArray();
    }

    // might be useful in other classes...
    public boolean isValidIndex(int index) {
        return index >= 0 && index < text.length();
    }

    public void performIndexCheck(int index) {
        if (!isValidIndex(index)) {
            throw new IndexOutOfBoundsException(
                    "Index " + index + " is out of bounds for source code of length "
                            + text.length() + " (source " + source.getName() + ")"
            );
        }
    }

    public Pair<Integer, Integer> calculateLineAndColumn(int index) {
        performIndexCheck(index);
        // binary search
        int low = 0;
        int high = newLineIndices.length - 1;
        while (low < high) {
            int mid = (low + high) / 2;
            if (newLineIndices[mid] < index) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return new Pair<>(low, index - newLineIndices[low] - 1);
    }

    public int numberOfLines() {
        return newLineIndices.length - 1;
    }

    public boolean isValidLine(int line) {
        return line >= 1 && line <= numberOfLines();
    }

    public void performLineCheck(int line) {
        if (!isValidLine(line)) {
            throw new IndexOutOfBoundsException(
                    "Line " + line + " is out of bounds for source code with  "
                            + (numberOfLines()) + " lines (source " + source.getName() + ")"
            );
        }
    }

    public int calculateIndex(int line, int column) {
        performLineCheck(line);
        int position;
        if (column <= 0 || (position = getLineStart(line) + column - 1) > newLineIndices[line]) {
            throw new IndexOutOfBoundsException(
                    "Column " + column + " is out of bounds for line " + line + " (source " + source.getName() + ")"
            );
        }
        return position;
    }

    public int getLineStart(int line) {
        performLineCheck(line);
        return newLineIndices[line - 1] + 1;
    }

    public int getLineEnd(int line) {
        performLineCheck(line);
        return newLineIndices[line] - 1;
    }

    public String readLine(int lineNumber) {
        performLineCheck(lineNumber);
        return text.substring(getLineStart(lineNumber), getLineEnd(lineNumber) + 1);
    }

    @Override
    public int length() {
        return text.length();
    }

    @Override
    public char charAt(int index) {
        performIndexCheck(index);
        return text.charAt(index);
    }

    @Override
    public String subSequence(int start, int end) {
        performIndexCheck(start);
        performIndexCheck(end);
        return (String) text.subSequence(start, end);
    }

    // includes the character at the end index
    public String slice(int start, int end) {
        return subSequence(start, end + 1);
    }
}
