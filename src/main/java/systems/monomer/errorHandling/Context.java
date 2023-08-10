package systems.monomer.errorHandling;


import systems.monomer.tokenizer.Source;

public class Context {
    private Index start, stop;
    private Source source;

    public Context(Index start, Index stop, Source source) {
        this.start = start;
        this.stop = stop;
        this.source = source;
    }

    public Context(int startRow, int startCol, int startPosition, int stopRow, int stopCol, int stopPosition, Source source) {
        this.start = new Index(startCol, startRow, startPosition);
        this.stop = new Index(stopCol, stopRow, stopPosition);
        this.source = source;
    }
    public Context(int startRow, int startCol, int stopRow, int stopCol, Source source) {
        this.start = new Index(startCol, startRow, -1);
        this.stop = new Index(stopCol, stopRow, -1);
        this.source = source;
    }

    public Source getSource() {
        return source;
    }

    public Index getStart() {
        return start;
    }

    public Index getStop() {
        return stop;
    }
}