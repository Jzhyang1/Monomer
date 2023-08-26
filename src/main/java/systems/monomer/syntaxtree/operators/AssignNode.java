package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.CompileMemory;
import systems.monomer.compiler.CompileSize;
import systems.monomer.compiler.CompileValue;
import systems.monomer.interpreter.InterpretTuple;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.interpreter.InterpretFunction;
import systems.monomer.interpreter.InterpretValue;
import systems.monomer.interpreter.InterpretVariable;
import systems.monomer.types.Type;
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

    /**
     * returns the type of the variable
     * @param potentialvar the variable
     * @param potentialval the value being assigned to the variable
     * @return the type
     */
    public static Type matchTypes(Node potentialvar, Node potentialval) {
        //TODO handle multiple assignment
        potentialval.matchTypes();
        potentialvar.matchTypes();
        if(potentialvar.getType() == null) {
            potentialvar.setType(potentialval.getType());
        } else if(potentialval.getType() == null) {
            potentialval.setType(potentialvar.getType());
        } else if(!potentialvar.getType().equals(potentialval.getType())) {
            potentialval.throwError("Type mismatch: " + potentialvar.getType() + " and " + potentialval.getType());
        }
        return potentialvar.getType();
    }

    public static InterpretValue assign(InterpretValue var, InterpretValue val) {
        if(var instanceof InterpretVariable variable) {
            variable.setValue(val);
            return variable;
        }
        else if (var instanceof InterpretTuple tuple) {
            if(val instanceof InterpretTuple valTuple) {
                for(int i = 0; i < tuple.size(); i++) {
                    assign(tuple.get(i), valTuple.get(i));
                }
            }
            return tuple;
        }
        throw new Error("Invalid assignment of " + val + " to " + var );
    }

    public void matchTypes() {
        if(functionKey != null) {
            Node first = getFirst();
            Node second = getSecond();
            CallNode callNode = (CallNode)first;
            TupleNode param = TupleNode.asTuple(callNode.getSecond());
            //TODO
//            functionKey.putOverload(new Signature(second.getType(), param.getType()), new InterpretFunction(param, second));
        }
        //TODO chained assignment
        //TODO function
        setType(matchTypes(getFirst(), getSecond()));

        //TODO match param (first.second) to some open callable

    }

    @Override
    public void matchVariables() {
        Node first = getFirst(), second = getSecond();
        if(first instanceof CallNode callNode) {
            Node identifier = callNode.getFirst(), args = callNode.getSecond();

            functionKey = new FunctionKey();

            //TODO name.getName() is temporary
            String name = identifier.getName();
            putVariable(name, functionKey);

            ModuleNode wrapper = new ModuleNode("function");
            wrapper.setParent(this);
            wrapper.with(args).with(second).matchVariables();
            functionKey.putOverload(args, second, wrapper);
        }
        else {
            super.matchVariables();
        }
    }

    public InterpretVariable interpretVariable() {
        if(functionKey != null) return functionKey;

        //needed for functions to work
        return getFirst().interpretVariable();  //TODO does this look right?
    }
    public InterpretValue interpretValue() {
        if(functionKey != null) return functionKey;

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
