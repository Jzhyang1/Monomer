package systems.monomer.variables;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.types.ObjectType;
import systems.monomer.types.Type;

import static systems.monomer.types.AnyType.ANY;

public abstract class Key extends InterpretVariable {

    public abstract Type getType();
    public abstract void setType(Type type);

    public boolean isConstant(){ return true; }
    public void setConstant(boolean constant) {
        throw new UnsupportedOperationException("Can not set constant on " + this);
    }

    public void put(String field, Type value) {
        Type type = this.getType();
        if(type == ANY) setType(type = new ObjectType());

        type.setField(field, value);
    }

    public abstract Operand getAddress();

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public String valueString() {
        InterpretValue value = getValue();
        return value == null ? "()" : value.valueString();
    }
}
