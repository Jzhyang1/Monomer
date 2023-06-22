package systems.merl.compiler.lexer;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import systems.merl.compiler.core.CodeRange;
import systems.merl.compiler.core.SourceCode;

import java.util.function.Predicate;

@RequiredArgsConstructor
public class Cursor {

    private final SourceCode sourceCode;
    private final int startingIndex;
    private final int lengthToRead;

    @Getter
    private int currentIndex = startingIndex;

    public int getRemainingLength() {
        return lengthToRead - (currentIndex - startingIndex);
    }

    public String readNextCharacters(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Cannot read negative number of characters" );
        }
        if (currentIndex + number > startingIndex + lengthToRead) {
            throw new IllegalArgumentException("Cannot read past the end of the cursor" );
        }
        return sourceCode.subSequence(currentIndex, currentIndex + number);
    }

    public char readNextCharacter() {
        if (currentIndex >= startingIndex + lengthToRead) {
            throw new IllegalArgumentException("Cannot read past the end of the cursor" );
        }
        return sourceCode.charAt(currentIndex);
    }

    public char advanceNextCharacter() {
        char c = readNextCharacter();
        currentIndex++;
        return c;
    }

    public String advanceNextCharacters(int number) {
        String s = readNextCharacters(number);
        currentIndex += number;
        return s;
    }

    public char previousCharacter() {
        if (currentIndex <= startingIndex) {
            throw new IllegalArgumentException("Cannot read before the start of the cursor" );
        }
        return sourceCode.charAt(currentIndex - 1);
    }

    public String readWhile(Predicate<Character> predicate) {
        StringBuilder sb = new StringBuilder();
        while (currentIndex < startingIndex + lengthToRead && predicate.test(sourceCode.charAt(currentIndex))) {
            sb.append(sourceCode.charAt(currentIndex));
            currentIndex++;
        }
        return sb.toString();
    }

    @Data
    public class CalculationKey {
        private final int startIndex;

        public int calculateLength() {
            return currentIndex - startIndex;
        }

        public String calculateString() {
            return sourceCode.subSequence(startIndex, currentIndex);
        }

        public CodeRange calculateCodeRange() {
            return new CodeRange(sourceCode.getSource(), startIndex, currentIndex - 1);
        }
    }

    public CalculationKey createCalculationKey() {
        return new CalculationKey(currentIndex);
    }

}
