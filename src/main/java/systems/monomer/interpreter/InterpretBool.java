package systems.monomer.interpreter;

public class InterpretBool extends InterpretValue {
    private boolean value;

    public InterpretBool(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public String valueString() {
        return Boolean.toString(value);
    }

    public InterpretBool clone() {
        return new InterpretBool(value);
    }
}
