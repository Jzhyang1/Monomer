package systems.monomer.syntaxtree.literals;

import systems.monomer.syntaxtree.ModuleNode;
import systems.monomer.syntaxtree.Node;
import systems.monomer.types.Type;
import systems.monomer.types.function.OverloadableType;
import systems.monomer.types.signature.Signature;
import systems.monomer.variables.FunctionBody;

import static systems.monomer.types.pseudo.AnyType.ANY;

public class FunctionBodyNode extends ModuleNode {
    public FunctionBodyNode(Node args, Node namedArgs, Node body) {
        super("function");
        add(args);
        add(namedArgs);
        add(body);
    }

    @Override
    public Node matchTypes() {

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
        return this;
    }
}
