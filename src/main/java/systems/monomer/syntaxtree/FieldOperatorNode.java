package systems.monomer.syntaxtree;

import systems.monomer.compiler.CompileMemory;
import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.variables.VariableKey;

public class FieldOperatorNode extends OperatorNode{
    private VariableKey key;

    public FieldOperatorNode(){
        super("field");
    }

    public VariableKey getVariableKey() {
        return key;
    }

    public void matchVariables() {
        //TODO this needs restructuring. Have getVariableKey -> getVariableEntry and
        // return the name and key of the Node, then use that name in the variable
        // name (ie "parentName.rhsName")
        VariableKey parentKey = getFirst().getVariableKey();
        VariableKey existing = getVariable(getName());
        if(existing == null) {
            existing = new VariableKey();
            putVariable(getName(), existing);
        }
        key = existing;
    }

    public InterpretVariable interpretVariable() {
        throw new Error("TODO unimplemented");
    }
    public InterpretValue interpretValue() {
        return null;
    }

    public CompileMemory compileMemory() {
        throw new Error("TODO unimplemented");
    }
    public CompileValue compileValue() {
        throw new Error("TODO unimplemented");
    }
    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
