package systems.monomer.errorhandling;

import lombok.Getter;
import lombok.Setter;
import systems.monomer.tokenizer.Source;

import java.util.List;

@Getter
public class ErrorBlock {
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
    public void throwError(String message) {
        StringBuilder errorMessage = new StringBuilder();
        Source source = context.getSource();
        Index start = context.getStart(), stop = context.getStop();

        //Error header
        errorMessage.append("ERROR ")
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
        for (int i = 0; i < lines.size(); i++) {
            String lineLabel = String.valueOf(start.getRow() + i);
            String line = lines.get(i);

            errorMessage.append(" ".repeat(padding - lineLabel.length()))
                    .append(lineLabel)
                    .append("| ")
                    .append(line)
                    .append("\n");

            int okStart = i == 0 ? start.getX() : 0;
            int okEnd = i == lines.size() - 1 ? stop.getX() : line.length();

            errorMessage.append(" ".repeat(padding))
                    .append("| ")
                    .append(" ".repeat(okStart))
                    .append("^".repeat(okEnd - okStart))
                    .append("\n");
        }

//        System.out.println(errorMessage.toString());
        throw new RuntimeException(errorMessage.toString());
    }
}
