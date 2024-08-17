package systems.monomer.syntaxtree.operators;

import org.jetbrains.annotations.Nullable;
import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.VariableNode;
import systems.monomer.syntaxtree.literals.StructureNode;
import systems.monomer.types.*;
import systems.monomer.types.function.OverloadableType;
import systems.monomer.types.function.OverloadsType;
import systems.monomer.types.object.ObjectType;
import systems.monomer.types.signature.Signature;
import systems.monomer.types.tuple.TupleType;
import systems.monomer.variables.FunctionBody;
import systems.monomer.variables.Key;

import java.util.List;

import static systems.monomer.execution.Handler.init;
import static systems.monomer.types.pseudo.AnyType.ANY;

public class AssignNode extends OperatorNode {
    protected static record FunctionInitInfo(Node identifier, Key function, Node args, StructureNode namedArgs, Node body,
                                           ModuleNode parent) {}
    protected static record FunctionSimplfiedInfo(OverloadsType overloads, int randomAccessIndex, Signature signature){}

    protected @Nullable FunctionInitInfo functionInit = null;
    protected FunctionSimplfiedInfo functionSimplified = null;
    protected boolean toLock = true;

    public AssignNode() {
        super("=");
    }

    @Override
    public void setIsExpression(boolean isExpression) {
        setThisExpression(isExpression);

        getFirst().setIsExpression(isExpression);
        //the RHS children are all expressions
    }

    @Override
    public void matchVariables() {
        Node first = getFirst(), second = getSecond();
        if (first instanceof CallNode callNode) {
            Node identifier = callNode.getFirst(), args = callNode.getSecond();
            Node namedArgs = callNode.size() == 2 ? init.emptyStructure() : callNode.get(2);
            if (!(namedArgs instanceof StructureNode)) throw namedArgs.syntaxError("Expected named args, got " + namedArgs);
            StructureNode namedArgsStruct = (StructureNode) namedArgs;
            namedArgsStruct.matchVariables();

            identifier.matchVariables();
            Key identifierKey = identifier.getVariableKey();

            ModuleNode wrapper = init.moduleNode("function");
            wrapper.setParent(this);
            for (String fieldName : namedArgsStruct.getFieldNames()) {
                wrapper.putVariable(fieldName, namedArgsStruct.getVariable(fieldName));
            }
            wrapper.with(args).with(namedArgs);
            wrapper.with(second).matchVariables();
            functionInit = new FunctionInitInfo(
                    identifier, identifierKey,
                    args, (StructureNode) namedArgs, second,
                    wrapper);
            return;
        }

        first.setIsDestination(true);
        if(second instanceof AssertTypeNode assertTypeNode &&
                    assertTypeNode.getFirst() instanceof VariableNode variableNode &&
                    "var".equals(variableNode.getName())) {
            set(1, second = assertTypeNode.getSecond());
            first.matchVariables();
//            first.getVariableKey().setConstant(false); //TODO set variable(s) to non-constant
            second.matchVariables();
            toLock = false;
        } else {
            super.matchVariables();
        }
    }

    public void matchTypes() {
        if (functionInit != null) {
            functionInit.identifier.matchTypes();

            Type potentialOverloads = functionInit.function.getType();
            OverloadableType overloads;
            if (potentialOverloads == ANY)
                functionInit.function.setType(overloads = new OverloadableType());
            else
                overloads = (OverloadableType) potentialOverloads;


            functionInit.namedArgs.matchTypes();
            functionInit.args.matchTypes();
            //TODO add a placeholder signature so that recursion doesn't loop forever
            functionInit.body.matchTypes();

            FunctionBody function = new FunctionBody(functionInit.args, functionInit.namedArgs, functionInit.body, functionInit.parent);

            //generate Signature from FunctionBody
            Signature signature = function.getType();

            //used for recursion TODO add back support for recursion
//            Signature tempSignature = new Signature(ANY, argsType, namedArgsType);

//            boolean needTempSignature = overloads.getOverload(tempSignature) == null;
//            if (needTempSignature)
//                overloads.putInterpretOverload(tempSignature, function);

            Type bodyType = functionInit.body.getType();

//            if(needTempSignature)
//                overloads.getOverloads().remove(tempSignature, function); //TODO remove placeholder signature used for recursion
//            Signature signature = new Signature(bodyType, argsType, namedArgsType);
            overloads.add(function);
            setType(overloads);

            functionSimplified = new FunctionSimplfiedInfo(null, -1, signature);
        } else {  //normal variable assignment
            List<Node> children = getChildren();
            Node value = children.get(children.size() - 1);
            value.matchTypes();
            Type valType = value.getType();

            for (int i = children.size() - 2; i >= 0; --i) {
                Node variable = children.get(i);

                Type variableType = variable.getType();
                variable.setType(variableType.testReplace(variableType.assign(valType)));
                variable.matchTypes();
            }
            setType(getFirst().getType());
        }
    }

    @Override
    public Node simplify() {
        if(functionInit != null) {
            //don't simplify OverloadableType by .simplify because that may produce a FunctionBody
            OverloadableType overloadable = (OverloadableType) functionInit.function.getType();
            OverloadsType overloads = new OverloadsType(overloadable.getOptions());
            functionInit.function.setType(overloads);
            int randomAccessIndex = overloads.randomAccessIndex(functionSimplified.signature);

            functionSimplified = new FunctionSimplfiedInfo(overloads, randomAccessIndex, functionSimplified.signature);
        }
        return this;
    }
}
