package systems.merl.monomer.tokenizer;

public class SourceString extends Source {
    public String value;

    public SourceString(String value) {
        this.value = value;
    }

    public String getTitle() {
        return "String source";
    }
    protected void bufferLines(int num) {
        throw new Error("TODO unimplemented");
    }
}
