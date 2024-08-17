package systems.monomer.variables;

import lombok.Getter;
import lombok.Setter;
import systems.monomer.errorhandling.ErrorBlock;
import systems.monomer.types.object.ObjectType;
import systems.monomer.types.Type;

import static systems.monomer.errorhandling.ErrorBlock.programError;
import static systems.monomer.types.pseudo.AnyType.ANY;

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
    public boolean isConstant() {
        return parent.isConstant();
    }

    public void setConstant(boolean constant) {
        Type parentType = parent.getType();
        if(!(parentType instanceof ObjectType ot)) {
            throw programError(parentType + " is not an object", ErrorBlock.Reason.SYNTAX);
        }
        ot.getField(name).setConstant(constant);
    }

    @Override
    public boolean isLocked() {
        return false; //TODO
    }
    @Override
    public void lock() {
        //TODO
    }

    @Override
    public Type getType() {
        Type parentType = parent.getType();
        if(!(parentType instanceof ObjectType ot)) {
            throw programError(parentType + " is not an object", ErrorBlock.Reason.SYNTAX);
        }
        return ot.getField(name);
    }
    @Override
    public void setType(Type type) {
        Type parentType = parent.getType();
        if(parentType instanceof ObjectType objectType)
            objectType.setField(name, type);
        else if(parentType == ANY) {
            ObjectType object = new ObjectType();
            object.setField(name, type);
            parent.setType(object);
        }
        else
            throw programError(parentType + " is not an object", ErrorBlock.Reason.SYNTAX);
    }
}
