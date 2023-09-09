package systems.monomer.interpreter;

import lombok.Getter;

@Getter
public class InterpretBreaking implements InterpretResult {
    private final InterpretValue value;
    private final String name;

    public InterpretBreaking(String name, InterpretValue value) {
        this.name = name;
        this.value = value;
    }

    public boolean isValue() {
        return false;
    }

    @Override
    public InterpretValue asValue() {
        return value;
    }
}
