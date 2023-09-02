package systems.monomer.errorhandling;


import lombok.Getter;
import systems.monomer.tokenizer.Source;

@Getter
public class Context {
    private final Index start, stop;
    private final Source source;

    public Context(Index start, Index stop, Source source) {
        this.start = start;
        this.stop = stop;
        this.source = source;
    }
}