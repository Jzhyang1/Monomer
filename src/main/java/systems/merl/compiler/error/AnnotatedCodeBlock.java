package systems.merl.compiler.error;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import systems.merl.compiler.core.CodeIndex;
import systems.merl.compiler.core.CodeRange;
import systems.merl.compiler.core.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AnnotatedCodeBlock {

    private final Source source;
    private final int startLine;
    private final int endLine;

    public static AnnotatedCodeBlock from(CodeRange range) {
        return new AnnotatedCodeBlock(range.getSource(), range.getStartLine(), range.getEndLine());
    }

    public static AnnotatedCodeBlock from(Source source, int startLine, int endLine) {
        return new AnnotatedCodeBlock(source, startLine, endLine);
    }

    @Data
    private static class Annotation {
        private final CodeRange range;
        private final String message;
        private final char indicator;
    }

    private final List<Annotation> annotations = new ArrayList<>();

    private void validate(CodeRange range) {
        if (!range.getSource().equals(source)) {
            throw new IllegalArgumentException("Code range must be from the same source" );
        }
        if (range.getStartLine() < startLine || range.getEndLine() > endLine) {
            throw new IllegalArgumentException("Code range must be within the annotated code block" );
        }
        if (!range.isSingleLine()) {
            throw new IllegalArgumentException("Code range must be a single line" );
        }
    }

    public AnnotatedCodeBlock annotate(CodeRange range, String message) {
        return annotate(range, message, '^');
    }

    public AnnotatedCodeBlock annotate(CodeRange range, String message, char indicator) {
        validate(range);
        annotations.add(new Annotation(range, message, indicator));
        return this;
    }

    public AnnotatedCodeBlock annotate(CodeIndex index, String message) {
        return annotate(index, message, '^');
    }

    public AnnotatedCodeBlock annotate(CodeIndex index, String message, char indicator) {
        return annotate(index.toSingleRange(), message, indicator);
    }

    // TODO
    private void renderLine(StringBuilder builder, int lineNumber, int padTo, List<Annotation> annotations) {
        builder.append(String.format("%" + padTo + "d | ", lineNumber));
        String line = source.getSourceCode().readLine(lineNumber);
        builder.append(line);
        builder.append('\n');
        if (annotations == null || annotations.isEmpty()) {
            return;
        }
        for (Annotation annotation : annotations) {
            // add spacing
            builder.append(String.format("%" + padTo + "s | ", "" ));
            for (int lineIndex = 0; lineIndex < annotation.range.getStartColumn() - 1; lineIndex++) {
                builder.append(line.charAt(lineIndex) == '\t' ? '\t' : ' ');
            }
            // add indicator
            for (int lineIndex = annotation.range.getStartColumn() - 1; lineIndex < annotation.range.getEndColumn() - 1; lineIndex++) {
                builder.append(line.charAt(lineIndex) == '\t' ? '\t' : annotation.getIndicator());
            }
            builder.append(" " );
            builder.append(annotation.getMessage());
            builder.append('\n');
        }
    }

    public String render(boolean skipEmptyLines) {
        StringBuilder builder = new StringBuilder();
        int padTo = String.valueOf(endLine).length();
        Map<Integer, List<Annotation>> annotationsByLine = new HashMap<>();

        builder.append(" ".repeat(padTo));
        builder.append(" | ");
        builder.append('\n');

        for (Annotation annotation : annotations) {
            for (int line = annotation.getRange().getStartLine(); line <= annotation.getRange().getEndLine(); line++) {
                annotationsByLine.computeIfAbsent(line, k -> new ArrayList<>()).add(annotation);
            }
        }
        if (!skipEmptyLines) {
            for (int line = startLine; line <= endLine; line++) {
                renderLine(builder, line, padTo, annotationsByLine.get(line));
            }

            builder.append(" ".repeat(padTo));
            builder.append(" | ");
            builder.append('\n');

            return builder.toString();
        }

        // not all the lines are annotated
        boolean[] array = new boolean[endLine - startLine + 1];
        for (Annotation annotation : annotations) {
            array[annotation.getRange().getStartLine() - startLine] = true;
            if (annotation.getRange().getStartLine() != startLine) {
                array[annotation.getRange().getStartLine() - startLine - 1] = true;
            }
            if (annotation.getRange().getEndLine() != endLine) {
                array[annotation.getRange().getEndLine() - startLine + 1] = true;
            }
        }

        int currentLine = startLine;
        while (currentLine <= endLine) {
            if (array[currentLine - startLine]) {
                renderLine(builder, currentLine, padTo, annotationsByLine.get(currentLine));
                currentLine++;
                continue;
            }
            int skipped = 0;
            while (currentLine + skipped <= endLine && !array[currentLine + skipped - startLine]) {
                skipped++;
            }

            if (skipped == 1) {
                renderLine(builder, currentLine, padTo, null);
                currentLine++;
                continue;
            }
            builder.append(" ".repeat(padTo - 1));
            builder.append("* | " );
            builder.append(" ... skipped " ).append(skipped).append(" line(s) ..." );
            builder.append('\n');
            currentLine += skipped;
        }


        builder.append(" ".repeat(padTo));
        builder.append(" | ");
        builder.append('\n');

        return builder.toString();
    }

    @Override
    public String toString() {
        return render(true);
    }
}
