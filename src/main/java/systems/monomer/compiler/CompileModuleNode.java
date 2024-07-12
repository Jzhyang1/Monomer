package systems.monomer.compiler;

import systems.monomer.compiler.assembly.Operand;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.util.Pair;
import systems.monomer.variables.VariableKey;

import java.util.ArrayList;
import java.util.List;

public class CompileModuleNode extends ModuleNode implements CompileNode {
    public CompileModuleNode(String name) {
        super(name);
    }

    public Operand compileValue(AssemblyFile file) {
        //globals
        //deal with constants by replacing the variable with the value if the value is less than 128 bits, otherwise treat as reserved-space values,
        // deal with dynamic variables by reserving spaces for size + pointer,
        // deal with fixed sized variables by reserving spaces for the values

        //a list of pairs of (address, min-size) for each dynamic variable
        List<Pair<Integer, Integer>> dynamicVariables = new ArrayList<>();
        int currentAddress = 0;
        for (VariableKey key : getVariables().values()) {
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

        for (CompileNode child : getChildrenCompileNodes())
            child.compileValue(file);

        //TODO not sure what this should return
        return new Operand(Operand.Type.MEMORY, null, 0, 0);
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }

    @Override
    public void compileVariables(AssemblyFile file) {
        //TODO implement
    }
}
