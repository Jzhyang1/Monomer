package systems.monomer.errorhandling;

import lombok.Getter;
import lombok.Setter;
import systems.monomer.tokenizer.Source;

import java.util.List;

@Getter
public class ErrorBlock {
    public static class SyntaxErrorException extends RuntimeException {
        SyntaxErrorException(String message) {
            super(message);
        }
    }

    public static class RuntimeErrorException extends RuntimeException {
        RuntimeErrorException(String message) {
            super(message);
        }
    }

    public static class ProgramErrorException extends RuntimeException {
        ProgramErrorException(String message) {
            super(message);
        }
    }

    @Setter
    private Context context = null;

    public void setContext(Index start, Index stop, Source source) {
        this.context = new Context(start, stop, source);
    }

    public Source getSource() {
        return context.getSource();
    }

    public Index getStart() {
        return context.getStart();
    }
    public Index getStop() {
        return context.getStop();
    }

    public SyntaxErrorException syntaxError(String message) {
        return new SyntaxErrorException(errorMessage("Syntax Error", message));
    }

    public RuntimeErrorException runtimeError(String message) {
        return new RuntimeErrorException(errorMessage("Runtime Error", message));
    }

    /**
     * For errors that are not the user's fault
     * @param message the internal error message
     * @return a RuntimeErrorException
     */
    public static ProgramErrorException programError(String message) {
        return new ProgramErrorException("Program Error: " + message);
    }

    private String errorMessage(String type, String message) {
        StringBuilder errorMessage = new StringBuilder();
        Source source = context.getSource();
        Index start = context.getStart(), stop = context.getStop();

        //Error header
        errorMessage.append(type)
                .append(" ")
                .append(message)
                .append(" in ")
                .append(source.getTitle())
                .append(":\n");

        //Padding for line number label
        int padding = String.valueOf(stop.getRow()).length();

        //Add an empty line marker to look better
        errorMessage.append(" ".repeat(padding))
                .append("|\n");

        //Add the error formatting
        List<String> lines = source.getCodeBlock(start.getY(), stop.getY());
        int size = lines.size();
        for (int i = 0; i < size; i++) {
            String lineLabel = String.valueOf(start.getRow() + i);
            String line = lines.get(i);

            errorMessage.append(" ".repeat(padding - lineLabel.length()))
                    .append(lineLabel)
                    .append("| ")
                    .append(line)
                    .append("\n");

            int okStart = i == 0 ? start.getX() : 0;
            int okEnd = i == size - 1 ? stop.getX() : line.length();

            errorMessage.append(" ".repeat(padding))
                    .append("| ")
                    .append(" ".repeat(okStart))
                    .append("^".repeat(okEnd - okStart))
                    .append("\n");
        }

        return errorMessage.toString();
    }
}
