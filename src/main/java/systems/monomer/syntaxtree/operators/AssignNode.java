package systems.monomer.syntaxtree.operators;

import systems.monomer.compiler.*;
import systems.monomer.compiler.Assembly.Operand;
import systems.monomer.interpreter.*;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.literals.StructureNode;
import systems.monomer.syntaxtree.literals.TupleNode;
import systems.monomer.types.*;
import systems.monomer.variables.Key;

import static systems.monomer.compiler.Assembly.Instruction.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static systems.monomer.types.AnyType.ANY;

public class AssignNode extends OperatorNode {
    private static record FunctionInitInfo(Node identifier, Key function, Node args, StructureNode namedArgs, Node body,
                                           ModuleNode parent) {
    }

    private FunctionInitInfo functionInit = null;

    public AssignNode() {
        super("=");
    }

    /**
     * returns the type of the variable
     *
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
        if (varType == ANY) {
            potentialvar.setType(valType);
        } else if (!valType.typeContains(varType)) {
            potentialval.syntaxError("Type mismatch: " + potentialvar.getType() + " and " + potentialval.getType());
            return null;
        }

        return type;
    }

    public static InterpretValue assign(Node dest, InterpretValue val) {
        dest.interpretAssign(val);
        return val;
    }

    @Override
    public void setIsExpression(boolean isExpression) {
        setThisExpression(isExpression);

        getFirst().setIsExpression(isExpression);
        //the RHS children are all expressions
    }

    public void matchTypes() {
        if (functionInit != null) {
            functionInit.identifier.matchTypes();

            Type potentialOverloads = functionInit.function.getType();
            OverloadedFunction overloads;
            if (potentialOverloads == ANY)
                functionInit.function.setType(overloads = new OverloadedFunction());
            else
                overloads = (OverloadedFunction) potentialOverloads;

            InterpretFunction function = new InterpretFunction(functionInit.args, functionInit.namedArgs, functionInit.body, functionInit.parent);

            functionInit.namedArgs.matchTypes();
            Type namedArgsType = functionInit.namedArgs.getType();

            functionInit.args.matchTypes();
            Type argsType = functionInit.args.getType();

            //used for recursion
            Signature tempSignature = new Signature(ANY, argsType, namedArgsType);

            boolean needTempSignature = overloads.getOverload(tempSignature) == null;
            if (needTempSignature)
                overloads.putSystemOverload(tempSignature, function);

            functionInit.body.matchTypes();
            Type bodyType = functionInit.body.getType();

//            if(needTempSignature)
//                overloads.getOverloads().remove(tempSignature, function); //TODO remove placeholder signature used for recursion
            Signature signature = new Signature(bodyType, argsType, namedArgsType);
            overloads.putSystemOverload(signature, function);

            setType(signature);
        } else {  //normal variable assignment
            List<Node> children = getChildren();
            Node value = children.get(children.size() - 1);
            value.matchTypes();
            Type valType = value.getType();

            for (int i = children.size() - 2; i >= 0; --i) {
                Node variable = children.get(i);

                //TODO check variable type
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

            //TODO match param (first.second) to some open callable
        }
    }

    @Override
    public void matchVariables() {
        Node first = getFirst(), second = getSecond();
        if (first instanceof CallNode callNode) {
            Node identifier = callNode.getFirst(), args = callNode.getSecond();
            Node namedArgs = callNode.size() == 2 ? StructureNode.EMPTY : callNode.get(2);
            if (!(namedArgs instanceof StructureNode)) namedArgs.syntaxError("Expected named args, got " + namedArgs);
            StructureNode namedArgsStruct = (StructureNode) namedArgs;
            namedArgsStruct.matchVariables();

            identifier.matchVariables();
            Key identifierKey = identifier.getVariableKey();
            assert identifierKey != null;

            ModuleNode wrapper = new ModuleNode("function");
            wrapper.setParent(this);
            for (String fieldName : namedArgsStruct.getFieldNames()) {
                wrapper.putVariable(fieldName, namedArgsStruct.getVariable(fieldName));
            }
            wrapper.with(args).with(namedArgs);//.matchVariables();    //TODO this solves a problem with not being able to reference types in args, but makes it so that the args can not have names that are the same as elsewhere
            wrapper.with(second).matchVariables();
            functionInit = new FunctionInitInfo(identifier, identifierKey, args, (StructureNode) namedArgs, second, wrapper);
        } else {
            super.matchVariables();
        }
    }

    public InterpretVariable interpretVariable() {
        //needed for functions to work
        return getFirst().interpretVariable();
    }

    public InterpretResult interpretValue() {
        if (functionInit == null) {
            InterpretResult ret = getSecond().interpretValue();
            if (ret.isValue()) {
                for (int i = size() - 2; i >= 0; --i)
                    get(i).interpretAssign(ret.asValue());
            }
            return ret;
        }
        return InterpretTuple.EMPTY;
    }

    public Operand compileValue(AssemblyFile file) {
        //TODO switch direction of assignment

        Operand dest = getFirst().compileValue(file);
        for (int i = 1; i < size(); ++i) {
            Operand src = get(i).compileValue(file);
            //TODO non-basic memory types
            file.add(MOV, src, dest);
            dest = src;
        }
        return dest;
    }

    public CompileSize compileSize() {
        throw new Error("TODO unimplemented");
    }
}
