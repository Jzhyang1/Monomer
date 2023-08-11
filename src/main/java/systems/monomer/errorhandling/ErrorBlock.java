package systems.monomer.errorhandling;

import systems.monomer.tokenizer.Source;

import java.util.List;

public class ErrorBlock {
    private Context context = null;

    public void setContext(Context context) {
        this.context = context;
    }
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

    public void throwError(String message) {
        StringBuilder errorMessage = new StringBuilder();
        Source source = context.getSource();
        Index start = context.getStart(), stop = context.getStop();

        //Error header
        errorMessage.append("ERROR ");
        errorMessage.append(message);
        errorMessage.append(" in ");
        errorMessage.append(source.getTitle());
        errorMessage.append(":\n");

        //Padding for line number label
        int padding = String.valueOf(stop.getRow()).length();

        //Add the error formatting
        List<String> lines = source.getCodeBlock(start.getY(), stop.getY());
        for (int i = 0; i < lines.size(); i++) {
            int lineLabel = start.getRow() + i;
            String line = lines.get(i);

            errorMessage.append(" ".repeat(padding - String.valueOf(lineLabel).length()));
            errorMessage.append(lineLabel);
            errorMessage.append("| ");
            errorMessage.append(line);
            errorMessage.append("\n");

            int okStart = i == 0 ? start.getX() : 0;
            int okEnd = i == lines.size() - 1 ? stop.getX() : line.length();

            errorMessage.append(" ".repeat(padding));
            errorMessage.append("| ");
            errorMessage.append(" ".repeat(okStart));
            errorMessage.append("^".repeat(okEnd - okStart + 1));
            errorMessage.append("\n");
        }

        throw new RuntimeException(errorMessage.toString());
    }
}
