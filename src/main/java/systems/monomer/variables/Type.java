package systems.monomer.variables;

import java.util.HashMap;
import java.util.Map;

public class Type implements Cloneable {
    private final Map<String, Type> fields = new HashMap<>();

    public void put(String field, Type value) {
        fields.put(field, value);
    }
    public Type get(String field) {
        return fields.get(field);
    }
    public boolean typeContains(Type type) {
        throw new Error("TODO unimplemented");
    }
    public Map<String, Type> getFields(){
        return fields;
    }

    public Type clone() {
        try {
            Type cloned = (Type)super.clone();
            fields.forEach((key, value) -> cloned.fields.put(key, value.clone()));
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
