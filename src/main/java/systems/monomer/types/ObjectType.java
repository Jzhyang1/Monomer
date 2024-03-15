package systems.monomer.types;

import lombok.Getter;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretObject;
import systems.monomer.interpreter.InterpretValue;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class ObjectType extends AnyType {
    public static final Type EMPTY = new ObjectType();
    private final Map<String, Type> fields = new HashMap<>();
    //non-constant-size fields are stored as size_and_pointers to the field
    private final Map<String, Integer> fieldOffsets = new HashMap<>();

    public static boolean typeInObject(Type type, Type maybeObject) {
        return maybeObject instanceof ObjectType object && object.fields.values().stream().anyMatch(type::equals);
    }

    @Override
    public boolean typeContains(Type type) {
        if(type instanceof ObjectType typeOther) {
            for(Map.Entry<String, Type> entry : typeOther.fields.entrySet()) {
                Type assignedValue = entry.getValue();
                Type value = fields.get(entry.getKey());
                if(value == null || !value.getType().typeContains(assignedValue.getType())) //This is the line that is different
                    return false;
            }
            return true;
        }
        return false;
    }
    public boolean typeConvertsTo(Type type) {
        if(type instanceof ObjectType typeOther) {
            for(Map.Entry<String, Type> entry : typeOther.fields.entrySet()) {
                Type assignedValue = entry.getValue();
                Type value = fields.get(entry.getKey());
                if(value == null || !assignedValue.getType().typeContains(value.getType())) //This is the line that is different
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

    public int getFieldOffset(String name) {
        //checks for fieldOffset otherwise generates it

        if(fieldOffsets.containsKey(name))
            return fieldOffsets.get(name);

        if(!fields.containsKey(name))
            throw new Error("Field " + name + " does not exist in " + this);

        //goes by sorted order of fields by name
        int offsetCounter = 0;
        int offsetLocation = -1;
        for(Map.Entry<String, Type> entry : fields.entrySet()) {
            if(entry.getKey().equals(name))
                offsetLocation = offsetCounter;

            CompileSize size = entry.getValue().compileSize();
            offsetCounter += size.getOccupiedStackSize();
        }

        return offsetLocation;
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

    @Override
    public String toString() {
        return super.toString() + valueString();
    }
}
