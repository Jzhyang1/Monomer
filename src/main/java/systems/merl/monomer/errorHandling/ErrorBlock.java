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
        // TODO format error
        throw new Error("unimplemented");
    }
}
