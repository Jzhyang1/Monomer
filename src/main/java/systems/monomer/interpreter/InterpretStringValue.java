package systems.monomer.interpreter;

public class InterpretStringValue extends InterpretValue {
    private String value;
    public InterpretStringValue(String value) {
        this.value = value;
    }
    public String valueString() {
        return value;
    }
}
