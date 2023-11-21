package systems.monomer.interpreter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InterpretSet extends InterpretCollection {

    private final Set<InterpretValue> set = new HashSet<>();

    public InterpretSet(List<? extends InterpretValue> list) {
        super(list.get(0));
        getValues().addAll(list);
    }

    public Collection<InterpretValue> getValues() {
        return set;
    }

    public InterpretSet clone() {
        throw new Error("TODO unimplemented");
    }

    @Override
    public String valueString() {
        return "{" + set.stream()
                .map(InterpretValue::valueString)
                .reduce((a, b) -> a + "," + b)
                .orElse("")
                + "}";
    }
}
