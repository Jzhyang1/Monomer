package systems.monomer.syntaxtree;

import lombok.Getter;
import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.types.Type;
import systems.monomer.variables.VariableKey;

import static systems.monomer.compiler.Assembly.Register.EBP;
import static systems.monomer.types.AnyType.ANY;

@Getter
public class VariableNode extends Node {
    private VariableKey variableKey = null;

    public VariableNode(String name) {
        super(name);
    }

    public Usage getUsage() {
        return Usage.IDENTIFIER;
    }

    public void matchVariables() {
        VariableKey existing = getVariable(getName());
        if (variableKey == null && existing == null)
            putVariable(getName(), variableKey = new VariableKey());
        else if (existing == null)
            putVariable(getName(), variableKey);
        else
            variableKey = existing;
    }

    public void matchTypes() {
        if (getType() == ANY)
            setType(variableKey.getType());
        else if(variableKey.getType() == ANY)
            variableKey.setType(getType());
        //TODO uncomment code below
//        else if(!key.getType().typeContains(getType()))
//            throwError("Type mismatch: " + getType() + " is not matchable to " + key.getType());
    }

    @Override
    public Type getType() {
        return variableKey == null ? ANY : variableKey.getType();
    }

    @Override
    public void setType(Type type) {
        if (variableKey == null) variableKey = new VariableKey();

        variableKey.setType(type);
    }

    public InterpretVariable interpretVariable() {
        return variableKey;
    }

    public InterpretValue interpretValue() {
        return variableKey.getValue();
    }

    @Override
    public void compileVariables(AssemblyFile file) {
        if(variableKey.getAddress() != null) return;
        if (variableKey.getType().isConstant())
            variableKey.setAddress(new Operand(
                    Operand.Type.MEMORY,
                    EBP,
                    file.incrementStackPosition(compileSize().getConstantSize()),
                    0));
        else
            variableKey.setAddress(new Operand(
                    Operand.Type.MEMORY_OF_POINTER,
                    EBP,
                    file.incrementStackPosition(Operand.SIZE_POINTER_SIZE),
                    0));
    }

    public Operand compileValue(AssemblyFile file) {
//        System.out.println("compiling variable " + getName() + " with address " + variableKey.getAddress(file));
        return variableKey.getAddress();
    }

    public CompileSize compileSize() {
        return variableKey.compileSize();
    }
}