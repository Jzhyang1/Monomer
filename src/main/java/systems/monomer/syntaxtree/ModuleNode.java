package systems.monomer.syntaxtree;

import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.compiler.AssemblyFile;
import systems.monomer.compiler.CompileSize;
import systems.monomer.interpreter.InterpretResult;
import systems.monomer.interpreter.InterpretTuple;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.util.Pair;
import systems.monomer.variables.VariableKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModuleNode extends Node {
    public ModuleNode(String name) {
        super(name);
    }

    public Usage getUsage() {
        return Usage.MODULE;
    }

    //variables map and overloaded getVariable to access the variables
    private final Map<String, VariableKey> variables = new HashMap<>();
    public void putVariable(String varName, VariableKey key) {
        variables.put(varName, key);
    }
    public VariableKey getVariable(String varName) {
        VariableKey ret = variables.get(varName);
        if(ret != null) return ret;
        return getParent() == null ? null : getParent().getVariable(varName);
    }
    public Map<String, VariableKey> getVariableValuesMap() {
        //TODO optimize based on whether constant
        return variables.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().clone(),
                        (a, b) -> b,
                        HashMap::new
                ));
    }
    public void setVariableValues(Map<String, VariableKey> values) {
        for(Map.Entry<String, VariableKey> entry : values.entrySet()) {
            VariableKey original = variables.get(entry.getKey());
            original.setValue(entry.getValue().getValue());
        }
    }

    public InterpretResult interpretValue() {
        List<InterpretValue> ret = new ArrayList<>();
        for(Node child : getChildren()) {
            InterpretResult result = child.interpretValue();
            if (!result.isValue()) return result;
            ret.add(result.asValue());
        }
        return new InterpretTuple(ret); //TODO use a different type than tuple
    }

    public Operand compileValue(AssemblyFile file) {
        //globals
        //deal with constants by replacing the variable with the value if the value is less than 128 bits, otherwise treat as reserved-space values,
        // deal with dynamic variables by reserving spaces for size + pointer,
        // deal with fixed sized variables by reserving spaces for the values

        //a list of pairs of (address, min-size) for each dynamic variable
        List<Pair<Integer, Integer>> dynamicVariables = new ArrayList<>();
        int currentAddress = 0;
        for (VariableKey key : variables.values()) {
            if (key.isConstant()) {
                currentAddress += key.compileSize().getConstantSize();
                key.setAddress(new Operand(Operand.Type.MEMORY, null, 0, currentAddress));
                //TODO then set the current address to the value of the constant
            } else if (key.getType().isConstant()) {    //TODO getType().isConstant() might be better if made into a method
                currentAddress += key.compileSize().getConstantSize();
                key.setAddress(new Operand(Operand.Type.MEMORY, null, 0, currentAddress));
            } else {
                Pair<Integer, Integer> address = new Pair<>(currentAddress, 0);    //TODO replace 0 with the initial size of the variable
                dynamicVariables.add(address);
                key.setAddress(new Operand(Operand.Type.MEMORY_OF_POINTER, null, 0, currentAddress));

                currentAddress += Operand.SIZE_SIZE + Operand.POINTER_SIZE;

                //TODO then set the current address to the address of the variable at run-time
            }
        }

        for (Node child : getChildren())
            child.compileValue(file);

        //TODO not sure what this should return
        return new Operand(Operand.Type.MEMORY, null, 0, 0);
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
