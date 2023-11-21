package systems.monomer.variables;

import lombok.Getter;
import lombok.Setter;
import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.types.AnyType;
import systems.monomer.types.Type;


@Getter @Setter
public class VariableKey extends Key {
    private InterpretValue value;
    private Type type = AnyType.ANY;

    private boolean isConstant = false; //TODO make this true by default
    private Operand address;

    public VariableKey(){}

//    public InterpretValue getValue() {
//        if(value == null)
//            value = type.defaultValue();
//        return value;
//    }


    public void setType(Type type) {
        value = type.defaultValue();
        this.type = type;
    }

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

    @Override
    public VariableKey clone() {
        VariableKey key = (VariableKey) super.clone();  //TODO also clone value, overloads, etc
        return key;
    }
}
