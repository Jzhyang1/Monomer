package systems.merl.monomer.errorHandling;

import systems.merl.monomer.tokenizer.Source;

public class ErrorBlock {
    private Source.Context context = null;

    public void setContext(Source.Context context) {
        this.context = context;
    }
    public void setContext(Source.Index start, Source.Index stop, Source source) {
        this.context = new Source.Context(start, stop, source);
    }

    public void throwError(String message) {
        // TODO
        StringBuilder errorMessage = new StringBuilder();

        errorMessage.append("ERROR ");
        errorMessage.append(message);
        errorMessage.append(" in ");
        errorMessage.append(context.getSource().getTitle());
        errorMessage.append(":\n");

        int lastlineNumber = context.getStop().getX();
        int padding = 0;
        for (int i = lastlineNumber; i > 0; i /= 10) {
            padding++;
        }

        for (int i = context.getStart().getX(); i <= context.getStop().getX(); i++) {
            errorMessage.append(" ".repeat(padding - String.valueOf(i).length()));
            errorMessage.append(i);
            errorMessage.append("| ");        
            errorMessage.append(context.getSource().getLine(i));
            errorMessage.append("\n");
            
            errorMessage.append(" ".repeat(padding));
            errorMessage.append("| ");
            errorMessage.append(" ".repeat(context.getStart().getY()));
            errorMessage.append("^".repeat(context.getStop().getY() - context.getStart().getY() + 1));
            errorMessage.append("\n");
        }
        
        // TODO format error
        //throw new Error("unimplemented");
    }
}
