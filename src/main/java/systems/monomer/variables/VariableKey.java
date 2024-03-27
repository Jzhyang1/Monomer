package systems.monomer.variables;

import lombok.Getter;
import lombok.Setter;
import systems.monomer.compiler.Assembly.Operand;
import static systems.monomer.compiler.Assembly.Register.*;
import static systems.monomer.errorhandling.ErrorBlock.programError;

import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.AnyType;
import systems.monomer.types.Type;


@Getter @Setter
public class VariableKey extends Key {
    private InterpretValue value;
    private Type type = AnyType.ANY;

    private boolean isConstant = true;
    private boolean isLocked = false;
    private Operand address;

    public VariableKey(){}


    @Override
    public void setField(String field, Type type) {
        this.type.setField(field, type);
        value.setField(field, type.defaultValue());
    }
    @Override
    public Type getField(String field) {
        return type.getField(field);
    }

    @Override
    public CompileSize compileSize() {
        return type.compileSize();
    }

    public String valueString() {
        return value.valueString();
    }

    public boolean isLocked() {
        return isLocked;
    }
    public void lock() {
        isLocked = true;
    }

    @Override
    public VariableKey clone() {
        VariableKey key = (VariableKey) super.clone();  //TODO also clone value, overloads, etc
        return key;
    }
}
