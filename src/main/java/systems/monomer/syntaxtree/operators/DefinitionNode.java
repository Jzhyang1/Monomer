package systems.monomer.syntaxtree.operators;

import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.syntaxtree.VariableNode;
import systems.monomer.syntaxtree.literals.FunctionBodyNode;
import systems.monomer.syntaxtree.literals.StructureNode;
import systems.monomer.types.Type;
import systems.monomer.types.function.OverloadableType;
import systems.monomer.types.pseudo.PlaceholderType;
import systems.monomer.types.signature.Signature;
import systems.monomer.variables.FunctionBody;
import systems.monomer.variables.Key;

import static systems.monomer.types.pseudo.AnyType.ANY;

//for function definitions
//the final form of AssignNode for functions
public class DefinitionNode extends OperatorNode {
    //2 children: VariableNode, FunctionBodyNode
    private Node args, namedArgs;   //here for easy access

    public DefinitionNode(CallNode head, Node body) {
        super("def");
        Node identifier = head.get(0);

        args = head.get(1);
        args.setIsDestination(true);

        namedArgs = head.size() == 2 ? env.emptyStructure() : head.get(2);
        namedArgs.setIsDestination(true);

        Node wrapper = new FunctionBodyNode(args, namedArgs, body); //TODO replace with env.functionBodyNode
        wrapper.setParent(this);

        this.add(identifier);
        this.add(wrapper);
    }

    @Override
    public void matchTypes() {
        getFirst().matchTypes();

        Key function = getFirst().getVariableKey();
        if(function == null) throw syntaxError("Expected function identifier, got " + getFirst());

        namedArgs.matchTypes();
        args.matchTypes();

        PlaceholderType retType = new PlaceholderType();
        Signature signature = new Signature(args.getType(), namedArgs.getType(), retType);

        Type potentialOverloads = function.getType();
        OverloadableType overloads;
        {
            if (potentialOverloads == ANY)
                function.setType(overloads = new OverloadableType());
            else
                overloads = (OverloadableType) potentialOverloads;
        }

        overloads.add(signature);

        getSecond().matchTypes();

        FunctionBody function = new FunctionBody(args, namedArgs, body, parent);

        //generate Signature from FunctionBody
        Signature signature = function.getType();

        //used for recursion TODO add back support for recursion
//            Signature tempSignature = new Signature(ANY, argsType, namedArgsType);

//            boolean needTempSignature = overloads.getOverload(tempSignature) == null;
//            if (needTempSignature)
//                overloads.putInterpretOverload(tempSignature, function);

//            if(needTempSignature)
//                overloads.getOverloads().remove(tempSignature, function); //TODO remove placeholder signature used for recursion
//            Signature signature = new Signature(bodyType, argsType, namedArgsType);
        overloads.add(function);
        setType(overloads);

        functionSimplified = new AssignNode.FunctionSimplfiedInfo(null, -1, signature);
    }
}