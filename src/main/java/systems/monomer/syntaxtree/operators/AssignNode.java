package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.*;
import systems.monomer.interpreter.*;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.VariableNode;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.types.*;
import systems.monomer.variables.Key;
import systems.monomer.variables.VariableKey;

import java.util.List;
import java.util.stream.IntStream;

import static systems.monomer.types.AnyType.ANY;

public class AssignNode extends OperatorNode {
    private static record FunctionInitInfo(Key function, Node args, Node body, ModuleNode parent) {};
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

        Type valType = potentialval.getType();
        Type varType = potentialvar.getType();

        Type type = valType;
        if(varType == ANY) {
            potentialvar.setType(valType);
        }
        else if(!valType.typeContains(varType)) {
            potentialval.throwError("Type mismatch: " + potentialvar.getType() + " and " + potentialval.getType());
            return null;
        }

        return type;
    }

    public static InterpretValue assign(Node dest, InterpretValue val) {
        if(dest instanceof TupleNode tupleDest && val instanceof InterpretTuple tupleVal) {
            InterpretTuple ret = new InterpretTuple(
                    IntStream.range(0, tupleDest.size())
                            .mapToObj(i -> assign(tupleDest.get(i), tupleVal.get(i)))
                            .toList()
            );
            return ret;
        }
        else if(dest instanceof VariableNode variable) {
            variable.interpretVariable().setValue(val);
            return val;
        }
        else {
            dest.throwError("Invalid assignment of " + val + " to " + dest.getType());
            return null;
        }
    }

    public void matchTypes() {
        if(functionInit != null) {
            OverloadedFunction overloads = (OverloadedFunction) functionInit.function.getType();
            //TODO rid of TupleNode.asTuple
            InterpretFunction function = new InterpretFunction(TupleNode.asTuple(functionInit.args), functionInit.body, functionInit.parent);

            functionInit.args.matchTypes();
            Type argsType = functionInit.args.getType();

            //used for recursion
            Signature tempSignature = new Signature(ANY, argsType);

            boolean needTempSignature = overloads.getOverload(tempSignature) == null;
            if(needTempSignature)
                overloads.putOverload(tempSignature, function);

            functionInit.body.matchTypes();
            Type bodyType = functionInit.body.getType();

            if(needTempSignature)
                overloads.getOverloads().remove(tempSignature, function);
            Signature signature = new Signature(bodyType, argsType);
            overloads.putOverload(signature, function);

            setType(signature);
        }
        else {
            List<Node> children = getChildren();
            Node value = children.get(children.size() - 1); value.matchTypes();
            Type valType = value.getType();

            for(int i = children.size() - 2; i >= 0; --i) {
                Node variable = children.get(i);

                variable.setType(valType);
                variable.matchTypes();

                Type varType = variable.getType();

//                if(varType == ANY) {
//                    variable.setType(valType);
//
//                    VariableKey varKey = variable.getVariableKey();
//                    VariableKey valKey = value.getVariableKey();
//                    if(varKey != null && valKey != null) {
//                        varKey.setOverloads(valKey.getOverloads());
//                    }
//                }
//                else if(!valType.typeContains(varType)) {
//                    value.throwError("Type mismatch: " + variable.getType() + " and " + value.getType());
//                }
            }
            //TODO chained assignment
//            setType(matchTypes(getFirst(), getSecond()));

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
            Key identifierKey = identifier.getVariableKey();
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
        return getFirst().interpretVariable();
    }

    public InterpretResult interpretValue() {
        if(functionInit == null) {
            InterpretResult ret = getSecond().interpretValue();
            if(ret.isValue()) {
                for(int i = getFirst().size() - 2; i >= 0; --i)
                    assign(getFirst().get(i), (InterpretValue) ret);
            }
            return ret;
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
