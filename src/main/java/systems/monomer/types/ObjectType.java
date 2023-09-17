package systems.monomer.types;

import lombok.Getter;
import systems.monomer.interpreter.InterpretObject;
import systems.monomer.interpreter.InterpretTuple;
import systems.monomer.interpreter.InterpretValue;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class ObjectType extends AnyType {
    private final Map<String, Type> fields = new HashMap<>();

    @Override
    public boolean typeContains(Type type) {
        if(type instanceof ObjectType typeOther) {
            for(Map.Entry<String, Type> entry : typeOther.fields.entrySet()) {
                Type assignedValue = entry.getValue();
                Type value = fields.get(entry.getKey());
                if(value == null || !value.getType().typeContains(entry.getValue().getType()))
                    return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean hasField(String field) {
        return fields.containsKey(field);
    }

    @Override
    public void setField(String field, Type value) {
        fields.put(field, value);
    }

    @Override
    public void assertField(String field, Type value) {
        if(fields.containsKey(field)) {
            Type type = fields.get(field);
            if(!type.typeContains(value))
                throw new Error("Field " + field + " does not contain " + value);
        }
        else
            fields.put(field, value);
    }

    @Override
    public Type getField(String field) {
        return fields.get(field);
    }

    @Override
    public Type clone() {
        ObjectType cloned = (ObjectType)super.clone();
        fields.forEach((key, value) -> cloned.fields.put(key, value.clone()));
        return cloned;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof ObjectType typeOther) && typeOther.fields.equals(fields);
    }

    @Override
    public String valueString() {
        return "{" + fields.entrySet().stream().map((entry)->entry.getKey()+"="+entry.getValue().valueString()).collect(Collectors.joining(",")) + "}";
    }

    @Override
    public InterpretValue defaultValue() {
        //InterpretObject with all fields set to their default values
        InterpretObject ret = new InterpretObject();
        fields.forEach((key, value) -> ret.set(key, value.defaultValue()));
        return ret;
    }
}
