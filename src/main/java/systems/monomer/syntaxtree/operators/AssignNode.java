package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.CompileMemory;
import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretTuple;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.variables.VariableKey;

public class AssignNode extends OperatorNode {
    public AssignNode() {
        super("=");
    }

    private Node matchTypes(Node potentialvar, Node potentialval) {
        //TODO handle multiple assignment
        potentialval.matchTypes();
        potentialvar.matchTypes();
        if(potentialvar.getType() == null) {
            potentialvar.setType(potentialval.getType());
        } else if(potentialval.getType() == null) {
            potentialval.setType(potentialvar.getType());
        } else if(!potentialvar.getType().equals(potentialval.getType())) {
            throwError("Type mismatch: " + potentialvar.getType() + " and " + potentialval.getType());
        }
        return null;
    }

    public void matchTypes() {
        //TODO chained assignment
        matchTypes(getFirst(), getSecond());
    }

    public InterpretVariable interpretVariable() {
        return getFirst().interpretVariable();
    }
    public InterpretValue interpretValue() {
        InterpretValue val = getSecond().interpretValue();
        getFirst().interpretVariable().setValue(val);
        return val;
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
