package systems.merl.monomer.errorHandling.error;
import systems.merl.monomer.tokenizer.Source;

public class SourceContext {
    private SourceIndex start;
    private SourceIndex stop;
    private Source source;

    public SourceContext(SourceIndex start, SourceIndex stop, Source source) {
        this.start = start;
        this.stop = stop;
        this.source = source;
    }

    public SourceContext(int startRow, int startCol, int stopRow, int stopCol, Source source) {
        this.start = new SourceIndex(startRow, startCol);
        this.stop = new SourceIndex(stopRow, stopCol);
        this.source = source;
    }
}