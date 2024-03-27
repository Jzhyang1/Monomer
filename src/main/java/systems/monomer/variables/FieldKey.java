package systems.monomer.variables;

import lombok.Getter;
import lombok.Setter;
import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.types.ObjectType;
import systems.monomer.types.Type;

import static systems.monomer.compiler.Assembly.Operand.Type.MEMORY;
import static systems.monomer.errorhandling.ErrorBlock.programError;
import static systems.monomer.types.AnyType.ANY;

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
        else {
            throw programError("Can not access field " + name + " of " + parent);
        }
    }

    @Override
    public boolean isConstant() {
        return parent.getField(name).isConstant();
    }

    public void setConstant(boolean constant) {
        ((Key) parent.getField(name)).setConstant(constant);
    }

    @Override
    public boolean isLocked() {
        return ((InterpretVariable) parent.getField(name)).isLocked();
    }
    @Override
    public void lock() {
        ((InterpretVariable) parent.getField(name)).lock();
    }

    @Override
    public Type getType() {
        return parent.getField(name).getType();
    }
    @Override
    public void setType(Type type) {
        if(parent.getType() instanceof ObjectType objectType)
            objectType.setField(name, type);
        else if(parent.getType() == ANY) {
            ObjectType object = new ObjectType();
            object.setField(name, type);
            parent.setType(object);
        }
        else
            throw new Error("TODO unimplemented");
    }

    @Override
    public Type getField(String field) {
        return getType().getField(field);
    }

    @Override
    public CompileSize compileSize() {
        return getType().compileSize();
    }

    @Override
    public FieldKey clone() {
        FieldKey key = (FieldKey) super.clone();  //TODO also clone value, etc
        return key;
    }

    public Operand getAddress() {
        Operand parentAddress = parent.getAddress();
        int childOffset = parent.getType().getFieldOffset(name);

        Operand fieldAddress = new Operand(MEMORY,
                    parentAddress.register,
                    parentAddress.offset + childOffset,
                    0);

        return fieldAddress;
    }
}
