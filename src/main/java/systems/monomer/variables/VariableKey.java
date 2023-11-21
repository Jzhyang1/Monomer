package systems.monomer.variables;

import lombok.Getter;
import lombok.Setter;
import systems.monomer.compiler.Assembly.Operand;
import static systems.monomer.compiler.Assembly.Register.*;

import systems.monomer.compiler.AssemblyFile;
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

    // TODO split getAddress into setting address and getting address
    //  then set all addresses before compiling other parts of the program
    public Operand getAddress(AssemblyFile file) {
        if(address != null) return address;
        if(type.isConstant())
            address = new Operand(
                    Operand.Type.MEMORY,
                    EBP,
                    file.incrementStackPosition(compileSize().getConstantSize()),
                    0);
        else
            address = new Operand(
                    Operand.Type.MEMORY_OF_POINTER,
                    EBP,
                    file.incrementStackPosition(Operand.SIZE_POINTER_SIZE),
                    0);

        return address;
    }

    @Override
    public VariableKey clone() {
        VariableKey key = (VariableKey) super.clone();  //TODO also clone value, overloads, etc
        return key;
    }
}
