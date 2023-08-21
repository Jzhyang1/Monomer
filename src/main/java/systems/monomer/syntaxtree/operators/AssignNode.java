package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.CompileMemory;
import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.*;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.variables.FunctionKey;
import systems.monomer.types.Signature;

public class AssignNode extends OperatorNode {
    /**
     * use only with function declarations
     */
    private FunctionKey functionKey = null;

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
        if(functionKey != null) {
            Node first = getFirst();
            Node second = getSecond();
            CallNode callNode = (CallNode)first;
            TupleNode param = TupleNode.asTuple(callNode.getSecond());
            functionKey.putOverload(new Signature(second.getType(), param.getType()), new InterpretFunction(param, second));
        }
        //TODO chained assignment
        //TODO function
        matchTypes(getFirst(), getSecond());

        //TODO match param (first.second) to some open callable

    }

    @Override
    public void matchVariables() {
        Node first = getFirst(), second = getSecond();
        if(first instanceof CallNode callNode) {
            ModuleNode wrapper = new ModuleNode("function");
            wrapper.setParent(this);
            wrapper.with(first).with(second).matchVariables();
            functionKey = callNode.getVariableKey();
        }
        else {
            super.matchVariables();
        }
    }

    public InterpretVariable interpretVariable() {
        throw new Error("TODO unimplemented");
//        return getFirst().interpretVariable();
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
