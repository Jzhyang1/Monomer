public class LineContext {
    private LineIndex start;
    private LineIndex stop;
    private Source source;

    public LineContext(LineIndex start, LineIndex stop, Source source) {
        this.start = start;
        this.stop = stop;
        this.source = source;
    }

    public LineContext(int startRow, int startCol, int stopRow, int stopCol, Source source) {
        this.start = new LineIndex(startRow, startCol);
        this.stop = new LineIndex(stopRow, stopCol);
        this.source = source;
    }
}