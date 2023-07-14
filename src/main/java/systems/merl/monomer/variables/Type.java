package systems.merl.monomer.variables;

import systems.merl.monomer.interpreter.InterpretValue;

import java.util.HashMap;
import java.util.Map;

public class Type {
    private final Map<String, Type> fields = new HashMap<>();

    public void put(String field, Type value) {
        fields.put(field, value);
    }
    public Type get(String field) {
        return fields.get(field);
    }
    public boolean typeContains(Type type) {
        throw new Error("unimplemented");
    }
    public Map<String, Type> getFields(){
        return fields;
    }
}
