package systems.monomer.syntaxtree.operators;

import systems.monomer.syntaxtree.Node;
import systems.monomer.types.object.ObjectType;
import systems.monomer.types.pseudo.OperatedType;
import systems.monomer.types.signature.Signature;
import systems.monomer.types.Type;

import java.util.List;

/**
 * A node representing a function call.
 *     <ul>
 *         <li>First child: the function to call</li>
 *         <li>Second child: the argument to pass to the function</li>
 *         <li>Third child: optional, the Structure representing named arguments</li>
 *     </ul>
 *     <br/>
 *     The type of the node is the return type of the function.
 *     the <b>getSignature</b> method returns the signature of the function call.
 */
public class CallNode extends OperatorNode {
    public CallNode() {
        super("call");
    }

    @Override
    public Node matchTypes() {
        super.matchTypes();
        Type argType = getSecond().getType();
        Type returnType = getType();
        Type namedArgType = size() > 2 ? get(2).getType() : new ObjectType();
        Signature signature = new Signature(argType, namedArgType, returnType);

        Node function = env.castToFunctionNode()
                .with(getContext())
                .with(signature)
                .with(getFirst())
                .matchTypes();
        set(0, function);
//        setType(((Signature)function.getType()).getRet());

        //TODO The merit of the below option is that it may handle recursive calls and forward references innately, but it is more expensive
        // the best option is to check if the full function signature is already known, and if so, use that.
        // In order to know if the function signature is complete, the CastToFunctionNode's signature might hold some information
        // e.g. a boolean for if the signature is currently being built, and if it isn't we can use the existing function signature.
        setType(new OperatedType(List.of(function.getType(), argType, namedArgType), types -> types.get(0).returnFor(types.get(1), types.get(2))));
        return this;
    }
}
