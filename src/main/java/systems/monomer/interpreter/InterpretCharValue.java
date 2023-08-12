package systems.monomer.interpreter;

public class InterpretCharValue extends InterpretValue {
    private char value;
    public InterpretCharValue(char value) {
        this.value = value;
    }
    public char getValue() {
        return value;
    }
    public String valueString() {
        return String.valueOf(value);
    }
}
