package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.*;
import systems.monomer.interpreter.*;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.types.*;
import systems.monomer.variables.VariableKey;

import java.util.List;

public class AssignNode extends OperatorNode {
    private static record FunctionInitInfo(VariableKey function, Node args, Node body, ModuleNode parent) {};
    private FunctionInitInfo functionInit = null;

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
        if(potentialvar.getType() == null || potentialvar.getType().equals(AnyType.ANY)) {  //TODO remove the AnyType.ANY condition
            potentialvar.setType(potentialval.getType());
        } else if(potentialval.getType() == null) {
            potentialval.setType(potentialvar.getType());
        } else if(!potentialvar.getType().equals(potentialval.getType())) {
            potentialval.throwError("Type mismatch: " + potentialvar.getType() + " and " + potentialval.getType());
        }
        return potentialvar.getType();
    }

    public static InterpretValue assign(InterpretValue dest, InterpretValue val) {
        if(dest instanceof InterpretVariable variable) {
            variable.setValue(val);
            if(val instanceof ObjectType valObj)
                variable.getFields().putAll(valObj.getFields());
            return variable;
        }
        else if (dest instanceof InterpretTuple tuple) {
            if(val instanceof InterpretTuple valTuple) {
                for(int i = 0; i < tuple.size(); i++) {
                    assign(tuple.get(i), valTuple.get(i));
                }
            }
            return tuple;
        }
        throw new Error("Invalid assignment of " + val + " to " + dest );
    }

    public void matchTypes() {
        if(functionInit != null) {
            //TODO rid of TupleNode.asTuple
            InterpretFunction function = new InterpretFunction(TupleNode.asTuple(functionInit.args), functionInit.body, functionInit.parent);

            functionInit.args.matchTypes();
            Type argsType = functionInit.args.getType();

            //used for recursion
            Signature tempSignature = new Signature(AnyType.ANY, argsType);
            boolean needTempSignature = functionInit.function.getOverload(tempSignature) == null;
            if(needTempSignature)
                functionInit.function.putOverload(tempSignature, function);

            functionInit.body.matchTypes();
            Type bodyType = functionInit.body.getType();

            if(needTempSignature)
                functionInit.function.getOverloads().remove(tempSignature, function);
            Signature signature = new Signature(bodyType, argsType);
            functionInit.function.putOverload(signature, function);

            setType(signature);
        }
        else {
            List<Node> children = getChildren();
            Node value = children.get(children.size() - 1);
            value.matchTypes();
            Type type = value.getType();
            for(int i = children.size() - 2; i >= 0; --i) {
                children.get(i).setType(type);
                children.get(i).matchTypes();
            }
            //TODO chained assignment
            setType(matchTypes(getFirst(), getSecond()));

            //TODO match param (first.second) to some open callable
        }
    }

    @Override
    public void matchVariables() {
        Node first = getFirst(), second = getSecond();
        if(first instanceof CallNode callNode) {
            Node identifier = callNode.getFirst(), args = callNode.getSecond();
//            isFunction = true;


            identifier.matchVariables();
            VariableKey identifierKey = identifier.getVariableKey();
            assert identifierKey != null;

            ModuleNode wrapper = new ModuleNode("function");
            wrapper.setParent(this);
            wrapper.with(args);//.matchVariables();    //TODO this solves a problem with not being able to reference types in args, but makes it so that the args can not have names that are the same as elsewhere
            wrapper.with(second).matchVariables();
            functionInit = new FunctionInitInfo(identifierKey, args, second, wrapper);
        }
        else {
            super.matchVariables();
        }
    }

    public InterpretVariable interpretVariable() {
        //needed for functions to work
        return getFirst().interpretVariable();  //TODO does this look right?
    }
    public InterpretValue interpretValue() {
        if(functionInit == null) {
            InterpretValue val = getSecond().interpretValue();
            getFirst().interpretVariable().setValue(val);
            return val;
        }
        return InterpretTuple.EMPTY;
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
