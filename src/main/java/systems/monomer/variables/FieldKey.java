package systems.monomer.variables;

import lombok.Getter;
import lombok.Setter;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.ObjectType;
import systems.monomer.types.Type;

@Getter @Setter
public class FieldKey extends Key {
    private Key parent;
    private String name;

    public FieldKey(String name){
        this.name = name;
    }
    public FieldKey(String name, Key parent){
        this.name = name;
        this.parent = parent;
    }

    @Override
    public InterpretValue getValue() {
        return parent.getValue().get(name);
    }
    @Override
    public void setValue(InterpretValue value) {
        if(parent.getValue() instanceof ObjectType objectType)
            objectType.setField(name, value);
        else
            throw new Error("TODO unimplemented");
    }

    @Override
    public Type getType() {
        return parent.getField(name).getType();
    }
    @Override
    public void setType(Type type) {
        parent.setField(name, type);
    }

    @Override
    public Type getField(String field) {
        return getType().getField(field);
    }

    @Override
    public FieldKey clone() {
        FieldKey key = (FieldKey) super.clone();  //TODO also clone value, etc
        return key;
    }
}
